import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author V. Levorato
 */
public class ProjectTools {
    
    /**
     * Returns an undirected graph by reading a .net file (pajek format).
     * @param fname file path
     * @return graph
     */
    public static Graph<String, Integer> readNet(String fname)
    {
        File f = new File(fname);
        BufferedReader reader = null;
        String line;

        Graph<String, Integer> G=new UndirectedSparseGraph<String, Integer>();

        try {
            reader = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException exc) {
            System.err.println("Error opening file .net\n");
        }


        ArrayList<String> V=new ArrayList<String>();

        try {

                line = reader.readLine();

                line = reader.readLine();
                //adding nodes
                while(!line.contains("*edgeslist"))
                {
                    StringTokenizer tok = new StringTokenizer(line, "\"");

                    tok.nextToken();
                    String v=tok.nextToken();
                    V.add(v);
                    G.addVertex(v);

                    line = reader.readLine();
                 }


                //adding edges
                int ne=0;
                line = reader.readLine();
                while(line!=null)
                {
                    StringTokenizer tok = new StringTokenizer(line, " ");
                    int s=Integer.valueOf(tok.nextToken());
                    while(tok.hasMoreTokens())
                    {
                        int t=Integer.valueOf(tok.nextToken());
                        if(!V.isEmpty() && G.findEdge(V.get(s-1),V.get(t-1))==null)
                            G.addEdge(ne,V.get(s-1),V.get(t-1));
                        else
                            G.addEdge(ne,Integer.toString(s),Integer.toString(t));
                        ne++;
                    }

                    line = reader.readLine();
                }

           reader.close();

        } catch (Exception e) {
            System.err.println("Error reading: " + e + "\n");
        }

     return G;
    
   }
    
     /**
     * Transforms a graph into a GraphViz format file.
     * @param fname Filename (without file extension)
     * @param G graph
     * @throws IOException 
     */
    public static void writeGV(String fname,Graph<String,Integer> G) throws IOException
    {
        
        //nodes and edges colors
        Color edgecolor=Color.BLUE;
        Color nodecolor=Color.GRAY;
        
        //open output stream
        PrintWriter pfile;
        pfile =  new PrintWriter(new BufferedWriter(new FileWriter(fname+".gv")));

        //header
        pfile.println("graph G {");
	pfile.println("size=\"100,40\"; ratio = auto;");
	pfile.println("node [style=filled];");

         //edges
         for(Integer e: G.getEdges())
            pfile.println("\""+G.getEndpoints(e).getFirst() +"\" -- \""+G.getEndpoints(e).getSecond() +"\" [color=\"#"+getHexcolor(edgecolor)+"\"];");
         

        //nodes
        for(String x : G.getVertices())
        {
            
            pfile.print("\""+x+"\" ");
                pfile.print("[color=\"#"+ getHexcolor(nodecolor) +"30\",fontcolor=black];\n");
   
        }

        pfile.println("}");
        pfile.flush();
        pfile.close();

     }
    
    /**
     * Returns the hexadecimal code from a Color object.
     * @param c Color to format
     * @return String of the color hexadecimal code
     */
    private static String getHexcolor(Color c)
    {
        String code="";
        code=Integer.toHexString(c.getRGB());
        code=code.substring(2);
        
        return code;
    }
    
    
}
