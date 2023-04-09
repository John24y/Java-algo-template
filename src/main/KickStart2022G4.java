package main;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

public class KickStart2022G4 {
    static StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
    static PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
    public static double nextDouble() throws IOException{ in.nextToken(); return in.nval; }
    public static float nextFloat() throws IOException{ in.nextToken(); return (float)in.nval; }
    public static int nextInt() throws IOException { in.nextToken(); return (int)in.nval; }
    public static String next() throws IOException{ in.nextToken(); return in.sval;}
    static BufferedReader re = new BufferedReader(new InputStreamReader(System.in));

    private void putRight(int x,long v,TreeMap<Integer,Long> map) {
        Map.Entry<Integer,Long> entry=null;
        while ((entry=map.ceilingEntry(x)) != null && entry.getValue()<v) {
            map.remove(entry.getKey());
        }
        map.put(x,v);
    }
    private void putLeft(int x,long v,TreeMap<Integer,Long> map) {
        Map.Entry<Integer,Long> entry=null;
        while ((entry=map.floorEntry(x)) != null && entry.getValue()<v) {
            map.remove(entry.getKey());
        }
        map.put(x,v);
    }

    public long solve(int n,long E) throws Exception {
        TreeMap<Integer, TreeMap<Integer,Integer>> points=new TreeMap<>();
        int maxX=0;
        for (int i = 0; i < n; i++) {
            int x=nextInt(),y=nextInt(),e=nextInt();
            TreeMap<Integer, Integer> map = points.computeIfAbsent(y, integer -> new TreeMap<>());
            map.put(x,e);
            maxX=Math.max(maxX,x);
        }
        TreeMap<Integer,Long> rightWard=new TreeMap<>();
        TreeMap<Integer,Long> leftWard=new TreeMap<>();
        //初始的位置远大于maxY，所以蝴蝶可以先调整方向
        long reachRightMax=0,reachLeftMax=-E;
        rightWard.put(0,0L);
        leftWard.put(maxX,-E);
        //从上到下一行一行的算，每算完一行更新reachRightMax,reachLeftMax
        for (TreeMap<Integer, Integer> map : points.descendingMap().values()) {
            //当前行向右，位置0，但没采当前行任何花朵
            putRight(0,Math.max(rightWard.get(0), reachLeftMax - E),rightWard);
            //当前行向右，开始采花
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                int x=entry.getKey(),e=entry.getValue();
                putRight(x,rightWard.floorEntry(x).getValue()+e,rightWard);
            }
            putLeft(maxX,Math.max(leftWard.get(maxX),reachRightMax-E),leftWard);
            for (Map.Entry<Integer, Integer> entry : map.descendingMap().entrySet()) {
                int x=entry.getKey(),e=entry.getValue();
                putLeft(x,leftWard.ceilingEntry(x).getValue()+e,leftWard);
            }
            reachRightMax=Math.max(reachRightMax,rightWard.lastEntry().getValue());
            reachLeftMax=Math.max(reachLeftMax,leftWard.firstEntry().getValue());
        }
        return Math.max(reachLeftMax,reachRightMax);
    }

    public static void main(String[] args) throws Exception {
        int T=nextInt();
        for (int i = 0; i < T; i++) {
            int n=nextInt();
            int E=nextInt();
            long r = new KickStart2022G4().solve(n,E);
            System.out.printf("Case #%d: %d\n", i+1,r);
        }
    }

}

