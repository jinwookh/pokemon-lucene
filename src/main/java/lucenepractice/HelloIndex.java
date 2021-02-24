package lucenepractice;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableFieldType;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

public class HelloIndex {

    public static void main(String[] args) throws IOException {
        File file = new File("hi");

        Directory directory = FSDirectory.open(file.toPath());

        IndexWriterConfig indexWriterConfig = new IndexWriterConfig();

        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

        FieldType type = new FieldType();
        type.setStored(true);

        Field field = new Field("star", "wars", type);

        Document document = new Document();
        document.add(field);

        indexWriter.addDocument(document);
        indexWriter.commit();

        System.out.println(indexWriter.numDocs());
    }
}
