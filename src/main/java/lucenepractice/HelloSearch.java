package lucenepractice;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

public class HelloSearch {

    public static void main(String[] args) throws IOException {

        File file = new File("hi");

        Directory directory = FSDirectory.open(file.toPath());

        DirectoryReader directoryReader = DirectoryReader.open(directory);

        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);

        Query query = new MatchAllDocsQuery();
        TopDocs search = indexSearcher.search(query, 50);
        Document doc = indexSearcher.doc(search.scoreDocs[43].doc);

        System.out.println(doc);

    }
}
