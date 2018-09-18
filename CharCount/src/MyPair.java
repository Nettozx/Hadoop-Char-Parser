import java.io.*;

import org.apache.hadoop.io.*;

public class MyPair implements WritableComparable<MyPair> {

  private Text docID;
  private IntWritable termFrequency;
  
  public MyPair() {
    set(new Text(), new IntWritable());
  }
  
  public MyPair(String docID, int termFrequency) {
    set(new Text(docID), new IntWritable(termFrequency));
  }
  
  public MyPair(Text docID, IntWritable termFrequency) {
    set(docID, termFrequency);
  }
  
  public void set(Text docID, IntWritable termFrequency) {
    this.docID = docID;
    this.termFrequency = termFrequency;
  }
  
  public Text getDocID() {
    return docID;
  }

  public IntWritable getTermFrequency() {
    return termFrequency;
  }

  @Override
  public void write(DataOutput out) throws IOException {
    docID.write(out);
    termFrequency.write(out);
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    docID.readFields(in);
    termFrequency.readFields(in);
  }
  
  @Override
  public int hashCode() {
    return docID.hashCode() * 163 + termFrequency.hashCode();
  }
  
  @Override
  public boolean equals(Object o) {
    if (o instanceof MyPair) {
      MyPair tp = (MyPair) o;
      return docID.equals(tp.docID);
    }
    return false;
  }

  @Override
  public String toString() {
    return docID.toString() + ":" + termFrequency.get();
  }
  
  @Override
  public int compareTo(MyPair tp) {
    int cmp = docID.compareTo(tp.docID);
    return cmp;
    //    if (cmp != 0) {
//      return cmp;
//    }
//    return termFrequency.compareTo(tp.termFrequency);
  }
}