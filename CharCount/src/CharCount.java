/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class CharCount {

	public static class TokenizerMapper 
	extends Mapper<Object, Text, Text, MyPair>{

		//private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();

		public void map(Object key, Text value, Context context
				) throws IOException, InterruptedException {
			StringTokenizer itr = new StringTokenizer(value.toString());

			while (itr.hasMoreTokens()) {
				String temp = itr.nextToken();
				String[] strArray = temp.split(":");
				word.set(strArray[1]);
				MyPair docFreq = new MyPair(strArray[0], Integer.parseInt(strArray[2]));

				context.write(word, docFreq);

			}
		}
	}

	public static class IntSumReducer 
	extends Reducer<Text,MyPair,Text,Text> {
		public void reduce(Text key, Iterable<MyPair> values, 
				Context context
				) throws IOException, InterruptedException {
			TreeMap<Integer, Integer> pairMap = new TreeMap<Integer, Integer>();
			//      ArrayList<MyPair> pairArray = new ArrayList<MyPair>();

			for (MyPair val : values) {
				//				MyPair tempMP = val;
				//				String tempID = val.getDocID().toString();
				//				if(key.toString().equals("presumed")){
				//				System.out.println("DOCID: " + val.getDocID().toString() + " == " + tempID);
				//				}
				//				if(!pairMap.containsKey(tempID)){
				//					pairMap.put(tempMP.getDocID().toString(), new MyPair(tempMP.getDocID(), tempMP.getTermFrequency()));
				//				}
				//				else{
				//					MyPair tempPair = new MyPair(tempID, val.getTermFrequency().get() + pairMap.get(tempID).getTermFrequency().get());
				//					pairMap.put(tempID, tempPair);
				//				}

				if(pairMap.containsKey(Integer.parseInt(val.getDocID().toString()))){
					pairMap.put(Integer.parseInt(val.getDocID().toString()), pairMap.get(val.getDocID().toString()) + val.getTermFrequency().get());
				}
				else{
					pairMap.put(Integer.parseInt(val.getDocID().toString()), val.getTermFrequency().get());
				}

			}
			StringBuilder sb = new StringBuilder();
			for(Entry<Integer, Integer> s : pairMap.entrySet()){
				if(sb.length() != 0)
					sb.append(",");
				sb.append(s.getKey() + ":" + s.getValue());
			}

			//			ArrayList<MyPair> pairList = new ArrayList<MyPair>(pairMap.values());
			//			context.write(key, new Text(pairList.toString().replaceAll("\\[|\\]|\\s", "")));
			context.write(key, new Text(sb.toString()));
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: wordcount <in> <out>");
			System.exit(2);
		}
		Job job = new Job(conf, "word count");
		job.setJarByClass(CharCount.class);
		job.setMapperClass(TokenizerMapper.class);
		//job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(MyPair.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
