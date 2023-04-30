package dssort;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DSsort {
	public static boolean removeDNC = false;
	public static void main(String[] args) {
		Path currentRelativePath = Paths.get("");
		String root = currentRelativePath.toAbsolutePath().toString();
		File input = new File(root + "/input");
		File output = new File(root + "/output");
		if (!input.exists()) {
			input.mkdir();
		}
		if (!output.exists()) {
			output.mkdir();
		}
		for (File file : input.listFiles()) {
            if (file.isDirectory()) {
                continue;
            }
            if (file.getName().toLowerCase().endsWith(".csv")) {
            	try {
            		 List<String> s1 = fileToList("./input/" + file.getName());
                     List<String> s2 = sortDSList(s1);
                     listToFile(".\\output\\" + file.getName(), s2);
        			//listToFile(".\\output\\" + file.getName(), 
        					//sortDSList(fileToList(".\\input\\" + file.getName())));
                     //System.out.println("s1: " + s1 + "\ns2: " + s2);
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
            }
		}
	}
	public static List<String> fileToList(String file) throws IOException {
		List<String> list = new ArrayList<>();
		String s = new String(downloadURL(new File(file).toURI().toURL()));
		String[] ss = s.split("\n");
		for (String e : ss) {
			list.add(e);
		}

		return list;

	}
	public static byte[] downloadURL(URL toDownload) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] chunk = new byte[4096];
		int bytesRead;

		URLConnection con = toDownload.openConnection();
		con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		con.setUseCaches(false);
		con.setDefaultUseCaches(false);
		con.connect();
		InputStream stream = con.getInputStream();
		while ((bytesRead = stream.read(chunk)) > 0) {
			outputStream.write(chunk, 0, bytesRead);
		}
		stream.close();
		return outputStream.toByteArray();
	}
	public static void listToFile(String file, List<String> s) throws MalformedURLException {
		StringBuilder sb = new StringBuilder();
		for (String ssss : s) {
			sb.append(ssss);
			sb.append("\n");
		}
		File f = new File(file);
		f.delete();
		try {
			FileOutputStream st = new FileOutputStream(f);
			st.write(sb.substring(0, sb.length() - 1).getBytes());
			st.flush();
			st.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static List<String> sortDSList(List<String> ss) { 
		List<String> listy = new ArrayList<String>();
		for(String s : ss) {
			if(s.contains("\"")) {
				//take the string, identify the part in quotation marks, split it by comma, then add each line with 
				//removed quotation marks and a single element from the split for each item in the split
				int quote1 = s.indexOf("\"");
				int quote2 = s.indexOf("\"", s.indexOf("\"") + 1);
				int quote3 = 0;
				int quote4 = 0;
				String ns;
				String phonenums1 = s.substring(quote1 + 1, quote2).replace(" ", "");
				String phonenums2 = "";
				String[] phonenumset1 = phonenums1.split(",");
				String[] phonenumset2 = {};
				//System.out.println("\nphone numbs 1:" + Arrays.toString(phonenumset1));
				if(s.substring(quote2 + 1).contains("\"")) { //checks for 2 phone number sets
					quote3 = s.indexOf("\"", quote2 + 1);
					quote4 = s.indexOf("\"", quote3 + 1);
					phonenums2 = s.substring(quote3 + 1, quote4).replace(" ","");
					phonenumset2 = phonenums2.split(",");
					//System.out.println("phone numbs 2:" + Arrays.toString(phonenumset2));
				} 
				if(phonenumset2.length == 0) {
					ns = s.substring(0,quote1) + "<<<Target1>>>" + s.substring(quote2 + 1);
				}
				else {
					ns = s.substring(0,quote1) + "<<<Target1>>>" + s.substring(quote2 + 1, quote3) + "<<<Target2>>>" + s.substring(quote4 + 1);
				}
				//System.out.println(ns);
				ArrayList<String> pns1 = new ArrayList<String>();
				ArrayList<String> pns2 = new ArrayList<String>();
				for(String num : phonenumset1) {
					if(num.contains("DNC") == false) {
						pns1.add(num);
					}
				}
				for(String num : phonenumset2) {
					if(num.contains("DNC") == false) {
						pns2.add(num);
					}
				}
				if(removeDNC) { 
					Object[] op1 = pns1.toArray();
					Object[] op2 = pns2.toArray();
					phonenumset1 = Arrays.copyOf(op1,op1.length, String[].class);
					phonenumset2 = Arrays.copyOf(op2,op2.length, String[].class);
				}
				int maxnum = (phonenumset1.length >= phonenumset2.length ? phonenumset1.length : phonenumset2.length);
				int minnum = (phonenumset1.length <= phonenumset2.length ? phonenumset1.length : phonenumset2.length);
				for(int i = 0; i < maxnum; i++) {
					if(i < minnum) {
						if(ns.contains("<<<Target2>>>")) {
							listy.add((ns.replace("<<<Target1>>>", phonenumset1[i]))
									.replace("<<<Target2>>>", phonenumset2[i]));
							//System.out.println((ns.replace("<<<Target1>>>", phonenumset1[i])).replace("<<<Target2>>>", phonenumset2[i]));
						}
						else {
							listy.add(ns.replace("<<<Target1>>>", phonenumset1[i]));
							//System.out.println(ns.replace("<<<Target1>>>", phonenumset1[i]));

						}
					}
					else {
						if(phonenumset2.length > phonenumset1.length) {
							if(ns.contains("<<<Target2>>>")) {
								listy.add((ns.replace("<<<Target1>>>", ""))
										.replace("<<<Target2>>>", phonenumset2[i]));
								//System.out.println((ns.replace("<<<Target1>>>", "")).replace("<<<Target2>>>", phonenumset2[i]));
							}
							else {
								listy.add(ns.replace("<<<Target1>>>", ""));
								//System.out.println(ns.replace("<<<Target1>>>", ""));
								
							}
						}
						else {
							if(ns.contains("<<<Target2>>>")) {
								listy.add((ns.replace("<<<Target1>>>", phonenumset1[i]))
										.replace("<<<Target2>>>", ""));
								//System.out.println((ns.replace("<<<Target1>>>", phonenumset1[i])).replace("<<<Target2>>>", ""));
							}
							else {
								listy.add(ns.replace("<<<Target1>>>", phonenumset1[i]));
								//System.out.println(ns.replace("<<<Target1>>>", phonenumset1[i]));
							}
						}		
					}
				}
			}
			else {
				listy.add(s);
			}
		}
		return listy;
	}

}
