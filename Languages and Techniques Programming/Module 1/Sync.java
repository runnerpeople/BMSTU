import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Sync {
        public static String S, D;
        public static List<Path> s = new ArrayList<Path>();
        public static List<Path> d = new ArrayList<Path>();
        public static final String sep = File.separator;

        public static void special_add(Path file) throws FileNotFoundException, IOException {
                if(s.contains(file.subpath(1, file.getNameCount()))) {					
                        if (Paths.get(D + sep + file.subpath(1, file.getNameCount())).toFile().length()!=Paths.get(S + sep + s.get(s.indexOf(file.subpath(1, file.getNameCount())))).toFile().length())
                                d.add(file.subpath(1, file.getNameCount()));
                        else {
                                int help, help2;
                                boolean flag = false;
                                InputStream myfile = new BufferedInputStream(new FileInputStream(Paths.get(D + sep + file.subpath(1, file.getNameCount())).toString()));
                                InputStream myfile2 = new BufferedInputStream(new FileInputStream(Paths.get(S + sep + s.get(s.indexOf(file.subpath(1, file.getNameCount())))).toString()));
                                help = myfile.read(); 
                                help2 = myfile2.read();
                                for (; help >= 0; help = myfile.read(), help2 = myfile2.read()) {
                                        if (help != help2) {
                                                d.add(file.subpath(1, file.getNameCount()));
						flag = true;
                                                break;
                                        }
                                }
                                if (flag == false)
				        s.remove(s.get(s.indexOf(file.subpath(1, file.getNameCount()))));				
                                }
		        }
		else
			d.add(file.subpath(1, file.getNameCount()));
        }

	public static void dirlist(String file) {
		String dirname = file;
		File f1 = new File(dirname);
		if (f1.isDirectory()) {
			String help[] = f1.list();
			for (int i=0; i < help.length; i++) {
				File f = new File(dirname + sep + help[i]);
				if (f.isDirectory())
					dirlist(dirname + sep + help[i]);
				else
					s.add(f.toPath().subpath(1,f.toPath().getNameCount()));
			}			
		}	
	}

	public static void dirlist_update(String file) throws FileNotFoundException, IOException {
		String dirname = file;
		File f1 = new File(dirname);
		if (f1.isDirectory()) {
			String help[] = f1.list();
			for (int i=0; i < help.length; i++) {
				File f = new File(dirname + sep + help[i]);
				if (f.isDirectory())
					dirlist_update(dirname + sep + help[i]);
				else {
 					special_add(f.toPath());
				}			
			}	
		}
	}	


        public static void main(String[] args) throws IOException {		
		S=args[0];
		D=args[1];                
                dirlist(S);
		dirlist_update(D);
                Collections.sort(s);
                Collections.sort(d);
                if (s.isEmpty() && d.isEmpty()) {
                        System.out.println("IDENTICAL");
                        System.exit(0);
                }
                for (int i=0;i<d.size();i++)
                	System.out.println("DELETE " + d.get(i));
                for (int i=0;i<s.size();i++)
                        System.out.println("COPY " + s.get(i)); 
        }
}
