import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PokemonSearchTest {


    @Test
    void 포켓몬_50개_이름_출력() throws IOException {
        File file = new File("hi");

        Directory directory = FSDirectory.open(file.toPath());

        DirectoryReader directoryReader = DirectoryReader.open(directory);

        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);

        Query query = new MatchAllDocsQuery();
        TopDocs search = indexSearcher.search(query, 50);

        for (int i = 0; i < 50; i++) {
            Document doc = indexSearcher.doc(search.scoreDocs[i].doc);
            System.out.println(doc.get("type1"));
        }
    }

    @Test
    void 포켓몬_개수는_801개() throws IOException {
        File file = new File("hi");

        Directory directory = FSDirectory.open(file.toPath());

        DirectoryReader directoryReader = DirectoryReader.open(directory);

        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);

        Query query = new MatchAllDocsQuery();
        int hits = indexSearcher.count(query);
        Assertions.assertThat(hits).isEqualTo(801);
    }

    @Test
    void 포켓몬_index의_모든_필드_출력() throws IOException {
        File file = new File("hi");

        Directory directory = FSDirectory.open(file.toPath());

        DirectoryReader directoryReader = DirectoryReader.open(directory);

        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);

        Document doc = indexSearcher.doc(1);
        List<IndexableField> fields = doc.getFields();

        for (IndexableField field : fields) {
            System.out.println("필드명: " + field.name() + ", 타입: " + field.fieldType());
        }
    }

    @Test
    void 악타입만_필터링() throws IOException {
        File file = new File("hi");

        Directory directory = FSDirectory.open(file.toPath());

        DirectoryReader directoryReader = DirectoryReader.open(directory);

        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);

        Term type1DarkTerm = new Term("type1", "dark");
        TermQuery type1DarkTermQuery = new TermQuery(type1DarkTerm);
        Term type2DarkTerm = new Term("type2", "dark");
        TermQuery type2DarkTermQuery = new TermQuery(type2DarkTerm);

        BooleanQuery query = new BooleanQuery.Builder()
                .add(type1DarkTermQuery, BooleanClause.Occur.SHOULD)
                .add(type2DarkTermQuery, BooleanClause.Occur.SHOULD)
                .build();

        TopDocs search = indexSearcher.search(query, 1000);

        for (int i = 0; i < 50; i++) {
            Document doc = indexSearcher.doc(search.scoreDocs[i].doc);
            System.out.println(doc.get("name"));
        }
    }


    @Test
    void 공격력이_가장_강한_악타입_포켓몬_5마리() throws IOException {
        File file = new File("hi");

        Directory directory = FSDirectory.open(file.toPath());

        DirectoryReader directoryReader = DirectoryReader.open(directory);

        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);

        Query query = new BooleanQuery.Builder().build();
        TopDocs search = indexSearcher.search(query, 50);

        for (int i = 0; i < 50; i++) {
            Document doc = indexSearcher.doc(search.scoreDocs[i].doc);
            System.out.println(doc.get("name"));
        }
    }
}
