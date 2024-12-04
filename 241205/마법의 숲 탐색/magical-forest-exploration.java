import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    private static int R, C, K;
    private static int[][] arr = new int[73][70];
    private static boolean[][] isExit = new boolean[73][70];
    private static int[] dx = {-1, 0, 1, 0};
    private static int[] dy = {0, 1, 0, -1};
    private static int ans = 0;
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static StringTokenizer st;

    static class Pos {
        int x;
        int y;

        public Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public String toString() {
            return "[x: " + x + ", y: " + y + "]";
        }
    }

    private static void reset() {
        for (int i = 0; i < R + 3; i++) {
            for (int j = 0; j < C; j++) {
                arr[i][j] = 0;
                isExit[i][j] = false;
            }
        }
    }

    private static boolean inRange(int x, int y) {
        return 3 <= x && x <= R + 3 - 1 && 0 <= y && y <= C - 1;
    }

    private static boolean canGoDown(int x, int y) {
        if (!(0 < x && x < R + 2 && 0 < y && y < C - 1)) {
            return false;
        }
        boolean flag = true;
        
        flag &= (arr[x][y - 1] == 0);
        flag &= (arr[x + 1][y] == 0);
        flag &= (arr[x][y + 1] == 0);
        return flag;
    }

    private static boolean canGoLeft(int x, int y) {
        if (!(0 < x && x < R + 2 && 0 < y && y < C - 1)) {
            return false;
        }
        boolean flag = true;
        flag &= (arr[x - 2][y] == 0);
        flag &= (arr[x][y] == 0);
        flag &= (arr[x + 1][y] == 0);

        flag &= (arr[x - 1][y - 1] == 0);
        flag &= (arr[x][y - 1] == 0);
        return flag;
    }

    private static boolean canGoRight(int x, int y) {
        if (!(0 < x && x < R + 2 && 0 < y && y < C - 1)) {
            return false;
        }
        boolean flag = true;

        flag &= (arr[x - 2][y] == 0);
        flag &= (arr[x][y] == 0);
        flag &= (arr[x + 1][y] == 0);

        flag &= (arr[x - 1][y + 1] == 0);
        flag &= (arr[x][y + 1] == 0);
        return flag;
    }

    private static int bfs(int x, int y) {
        int maxRow = 0;
        boolean[][] visited = new boolean[73][70];
        Queue<Pos> q = new ArrayDeque<>();
        q.offer(new Pos(x, y));
        visited[x][y] = true;
        while(!q.isEmpty()) {
            Pos pos = q.poll();
            int nowX = pos.x;
            int nowY = pos.y;
            for (int i = 0; i < 4; i++) {
                int nextX = nowX + dx[i];
                int nextY = nowY + dy[i];
                if (inRange(nextX, nextY) && !visited[nextX][nextY] && (arr[nextX][nextY] == arr[nowX][nowY] || (
                    arr[nextX][nextY] != 0 && isExit[nowX][nowY]))) {
                    q.offer(new Pos(nextX, nextY));
                    visited[nextX][nextY] = true;
                    maxRow = Math.max(maxRow, nextX);
                }
            }
        }
        return maxRow;
    }

    private static void down(int nowX, int nowY, int dir, int id) {
        if (canGoDown(nowX + 1, nowY)) {
            down(nowX + 1, nowY, dir, id);
        }
        else if (canGoLeft(nowX + 1, nowY - 1)) {
            down(nowX + 1, nowY - 1, (dir + 3) % 4, id);
        }
        else if (canGoRight(nowX + 1, nowY + 1)) {
            down(nowX + 1, nowY + 1, (dir + 1) % 4, id);
        }
        else {
            if (!inRange(nowX - 1, nowY) || !inRange(nowX + 1, nowY)) {
                reset();
            }
            else {
                arr[nowX][nowY] = id;
                for (int i = 0; i < 4; i++) {
                    arr[nowX + dx[i]][nowY + dy[i]] = id;
                }
                isExit[nowX + dx[dir]][nowY + dy[dir]] = true;
                ans += (bfs(nowX, nowY) - 3 + 1);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        st = new StringTokenizer(br.readLine());
        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        for (int i = 1; i <= K; i++) {
            st = new StringTokenizer(br.readLine());
            int startY = Integer.parseInt(st.nextToken());
            int dir = Integer.parseInt(st.nextToken());
            down(1, startY - 1, dir, i);
        }
        System.out.println(ans);
    }
}