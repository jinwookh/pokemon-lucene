package lucenepractice.pokemon;

import com.opencsv.CSVReader;
import lucenepractice.Typetype;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleDocValuesField;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PokemonIndexer {
    public static void main(String[] args) throws IOException {

        File file = new File("pokemon.csv");

        FileReader fileReader = new FileReader(file);
        CSVReader csvReader = new CSVReader(fileReader);

        List<String[]> pokemonList = csvReader.readAll();

        String[] statTitles = pokemonList.get(0);

        indexPokemons(pokemonList, statTitles);

        System.out.println(pokemonList);

    }

    private static void indexPokemons(List<String[]> pokemonList, String[] statNames) throws IOException {
        IndexWriter indexWriter = getIndexWriter();

        for (int i = 1; i < pokemonList.size(); i++) {
            List<String> singlePokemon = Arrays.asList(pokemonList.get(i));
            indexSinglePokemon(statNames, indexWriter, singlePokemon);
        }
    }

    private static void indexSinglePokemon(String[] statNames, IndexWriter indexWriter, List<String> singlePokemon) throws IOException {
        Document document = new Document();
        for (int i = 0; i < singlePokemon.size(); i++) {
            indexSinglePokemonStat(singlePokemon.get(i), statNames[i], document);
        }
        indexSinglePokemonSource(singlePokemon, document);
        indexWriter.addDocument(document);
        indexWriter.commit();
    }

    private static void indexSinglePokemonSource(List<String> singlePokemon, Document document) {
        FieldType type = new FieldType();
        type.setStored(true);

        Field field = new StringField("_source", singlePokemon.toString(), Field.Store.YES);
        document.add(field);
    }

    private static void indexSinglePokemonStat(String pokemonStat, String statName, Document document) throws IOException {
        if (statName.equals("abilities")) {
            indexAbilitiesStat(pokemonStat, statName, document);
            return;
        }
        // "['Overgrow', 'Chlorophyll']"

        Typetype typetype = checkType(pokemonStat);
        indexPokemonStat(typetype, pokemonStat, document, statName);
    }

    private static void indexAbilitiesStat(String pokemonStat, String statName, Document document) {
        String processed1 = pokemonStat.substring(1, pokemonStat.length() - 1);
        String processed2 = processed1.replace("\'", "");
        String[] splittedString = processed2.split(",");
        List<String> abilitiesStat = Arrays.stream(splittedString).map(String::trim).collect(Collectors.toList());

        for (String abilityStat : abilitiesStat) {
            indexString(document, abilityStat, statName);
        }
    }

    private static void indexPokemonStat(Typetype typetype, String pokemonStatValue, Document document, String statFieldName) {
        if (typetype.equals(Typetype.Double)) {
            indexDouble(document, pokemonStatValue, statFieldName);
            return;
        }

        if (typetype.equals(Typetype.Integer)) {
            indexInteger(document, pokemonStatValue, statFieldName);
            return;
        }

        indexString(document, pokemonStatValue, statFieldName);
    }

    private static void indexString(Document document, String pokemonStatValue, String statFieldName) {
        FieldType type = new FieldType();
        type.setStored(true);

        Field field = new StringField(statFieldName, pokemonStatValue, Field.Store.YES);
        document.add(field);
    }

    private static void indexInteger(Document document, String pokemonStatValue, String statFieldName) {

        IntPoint intPoint = new IntPoint(statFieldName, Integer.parseInt(pokemonStatValue));
        document.add(intPoint);
    }

    private static void indexDouble(Document document, String pokemonStatValue, String statFieldName) {
        NumericDocValuesField field = new DoubleDocValuesField(statFieldName, Double.parseDouble(pokemonStatValue));
        DoublePoint doublePoint = new DoublePoint(statFieldName, Double.parseDouble(pokemonStatValue));
        StoredField storedField = new StoredField(statFieldName, Double.parseDouble(pokemonStatValue));

        document.add(field);
        document.add(doublePoint);
        document.add(storedField);
    }

    private static Typetype checkType(String pokemonStat) {

        if (pokemonStat.isEmpty()) {
            return Typetype.String;
        }

        try {
            double doubleValue = Double.valueOf(pokemonStat);
            return Typetype.Double;
        } catch (Exception e) {

        }

        return Typetype.String;
    }

    private static IndexWriter getIndexWriter() throws IOException {
        File file = new File("hi");

        Directory directory = FSDirectory.open(file.toPath());

        IndexWriterConfig indexWriterConfig = new IndexWriterConfig();

        return new IndexWriter(directory, indexWriterConfig);
    }
}
