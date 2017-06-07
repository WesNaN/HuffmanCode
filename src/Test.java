import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Test
{

    public static byte[] readBytesFromFile(String inputFile)
    {
        InputStream inputStream;
        byte[] bytes;

        try
        {
            inputStream = new FileInputStream(inputFile);
            long fileSize = new File(inputFile).length();

            bytes = new byte[(int) fileSize];
            inputStream.read(bytes);

            return bytes;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static HashMap<Character, Integer> buildHashMapFromBytes(byte[] bytes)
    {
        long fileSize = bytes.length;

        ArrayList<Byte> bytesArray = new ArrayList<>();

        for (int i = 0; i < fileSize; i++)
        {
            Byte current = bytes[i];

            if (!bytesArray.contains(current))
            {
                bytesArray.add(current);
            }
        }

        int values[] = new int[bytesArray.size()];
        byte bytesSingle[] = new byte[bytesArray.size()];

        for(int i = 0; i < bytesArray.size(); i++)
        {
            bytesSingle[i] = bytesArray.get(i);
        }

        for (int k = 0; k < bytesArray.size(); k++)
        {
            Byte b = bytesArray.get(k);

            int count = 0;
            for (int i = 0; i < fileSize; i++)
            {
                if (b.equals(bytes[i]))
                {
                    count++;
                }
            }

            values[k] = count;
        }

//        for(int i = 0; i < bytesArray.size(); i++)
//        {
//            System.out.println("Byte " + (char) bytesSingle[i] + " poped: " + values[i]);
//        }

        HashMap<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < values.length; i++)
        {
            map.put((char) bytesSingle[i], values[i]);
        }

        return map;
    }

    public static void main(String args[])
    {

        String inputFile = "Test";

        byte[] bytes = readBytesFromFile(inputFile);
        assert (bytes != null);
        HashMap<Character, Integer> map = buildHashMapFromBytes(bytes);


        Node root = buildTree(map);
        System.out.println();
        Map<Character, String> codeMap = generateCode(root);

        writeCodeToFile(codeMap, bytes, "out.huff");
        saveHashMap(codeMap, "CodeMap.huff");

        readCodeFromFile("CodeMap.huff", "out.huff");


    }

    public static void readCodeFromFile(String mapFile, String codeFile)
    {
        Map<Character, String> codeMap;

        try
        {
            FileInputStream fileIn = new FileInputStream(mapFile);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            codeMap = (HashMap<Character, String>) in.readObject();
            in.close();
            fileIn.close();

            for (Map.Entry<Character, String> entry : codeMap.entrySet())
            {
                System.out.println("Char : " + entry.getKey() + " code : " + entry.getValue());
            }

            String contents = new String(Files.readAllBytes(Paths.get(codeFile)));

            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < contents.length(); i++)
            {
                sb.append(contents.charAt(i));

                String x = sb.toString();
                for (Map.Entry<Character, String> entry : codeMap.entrySet())
                {
                    if (Objects.equals(x, entry.getValue()))
                    {
                        System.out.print(entry.getKey());

                        sb.setLength(0);
                    }

                }

            }





        }
        catch (IOException | ClassNotFoundException i)
        {
            i.printStackTrace();
        }
    }

    public static void saveHashMap(Map<Character, String> codeMap, String inputFile)
    {
        FileOutputStream fileOut;
        try
        {
            fileOut = new FileOutputStream(inputFile);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(codeMap);
            out.close();
            fileOut.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void writeCodeToFile(Map<Character, String> codeMap, byte[] bytes, String inputFile)
    {
        try (PrintWriter out = new PrintWriter(inputFile))
        {
            for (Byte b : bytes)
            {
                String s = codeMap.get((char) b.byteValue());
                out.print(s);
                System.out.println(s);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


    }


    public static Node buildTree(HashMap<Character, Integer> map)
    {
        PriorityQueue<Node> queue = new PriorityQueue<>();

        for (char c : map.keySet())
        {
            queue.add(new Node(c, map.get(c), null, null));
        }

        while (queue.size() > 1)
        {
            Node left = queue.poll();
            Node right = queue.poll();

            queue.add(new Node('#', left.freq + right.freq, left, right));
        }

        return queue.poll();
    }

    public static void show(Node tmp)
    {

        if (tmp == null)
        {
            return;
        }

        show(tmp.left);
        System.out.println("Char: " + tmp.character + " Freq: " + tmp.freq);
        show(tmp.right);
    }

    public static Map<Character, String> generateCode(Node root)
    {
        Map<Character, String> map = new HashMap<>();
        genCodeSupport(root, map, new StringBuilder());
        return map;
    }

    public static void genCodeSupport(Node tmp, Map<Character, String> map, StringBuilder sb)
    {
        if (tmp.isLeaf())
        {
            map.put(tmp.character, sb.toString());
            return;
        }

        sb.append("0");
        genCodeSupport(tmp.left, map, sb);
        sb.deleteCharAt(sb.length() - 1);

        sb.append("1");
        genCodeSupport(tmp.right, map, sb);
        sb.deleteCharAt(sb.length() - 1);
    }

}
