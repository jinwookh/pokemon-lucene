import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
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
    void 악타입만_포켓몬() throws IOException {
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
    void 공격력이_120_이상_땅타입_포켓몬() throws IOException {
        File file = new File("hi");

        Directory directory = FSDirectory.open(file.toPath());

        DirectoryReader directoryReader = DirectoryReader.open(directory);

        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);

        Term type1DarkTerm = new Term("type1", "ground");
        TermQuery type1DarkTermQuery = new TermQuery(type1DarkTerm);
        Term type2DarkTerm = new Term("type2", "ground");
        TermQuery type2DarkTermQuery = new TermQuery(type2DarkTerm);
        Query doubleQuery = DoublePoint.newRangeQuery("attack", 120, 130);

        BooleanQuery darkTypeQuery = new BooleanQuery.Builder()
                .add(type1DarkTermQuery, BooleanClause.Occur.SHOULD)
                .add(type2DarkTermQuery, BooleanClause.Occur.SHOULD)
                .build();


        BooleanQuery outerQuery = new BooleanQuery.Builder()
                .add(darkTypeQuery, BooleanClause.Occur.FILTER)
                .add(doubleQuery, BooleanClause.Occur.FILTER)
                .build();

        TopDocs search = indexSearcher.search(outerQuery, 1000);

        for (int i = 0; i < search.totalHits; i++) {
            Document doc = indexSearcher.doc(search.scoreDocs[i].doc);
            System.out.println(doc.get("name"));
        }
    }

    @Test
    void 공격력이_가장_큰_불타입_포켓몬_5마리() throws IOException {
        File file = new File("hi");

        Directory directory = FSDirectory.open(file.toPath());

        DirectoryReader directoryReader = DirectoryReader.open(directory);

        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);

        Term type1Term = new Term("type1", "fire");
        TermQuery type1TermQuery = new TermQuery(type1Term);
        Term type2Term = new Term("type2", "fire");
        TermQuery type2TermQuery = new TermQuery(type2Term);

        BooleanQuery fireTypeQuery = new BooleanQuery.Builder()
                .add(type1TermQuery, BooleanClause.Occur.SHOULD)
                .add(type2TermQuery, BooleanClause.Occur.SHOULD)
                .build();


        BooleanQuery outerQuery = new BooleanQuery.Builder()
                .add(fireTypeQuery, BooleanClause.Occur.FILTER)
                .build();

        SortField sortField = new SortField("attack", SortField.Type.DOUBLE, true);
        Sort sort = new Sort(sortField);

        TopDocs search = indexSearcher.search(outerQuery, 1000, sort);

        for (int i = 0; i < search.totalHits; i++) {
            Document doc = indexSearcher.doc(search.scoreDocs[i].doc);
            System.out.println(doc.get("name") + doc.get("attack"));
        }
    }

    @Test
    void Natural_Cure_능력을_가진_포켓몬() throws IOException {
        // TODO
    }
}
