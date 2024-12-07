import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static StringTokenizer st;
    private static int L, N, Q;
    private static int[][] arr = new int[41][41];
    private static int[] r = new int[31], c = new int[31], h = new int[31], w = new int[31], k = new int[31], kBefore = new int[31];
    private static int[] nextR = new int[31], nextC = new int[31], nextK = new int[31];
    private static boolean[] isMoved = new boolean[31];
    private static int[] dmg = new int[31];
    private static int[] dx = {-1, 0, 1, 0};
    private static int[] dy = {0, 1, 0, -1};

    public static void main(String[] args) throws Exception {
        st = new StringTokenizer(br.readLine());
        L = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());
        for (int i = 1; i <= L; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 1; j <= L; j++) {
                arr[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        for (int i = 1; i <= N; i++) {
            st = new StringTokenizer(br.readLine());
            r[i] = Integer.parseInt(st.nextToken());
            c[i] = Integer.parseInt(st.nextToken());
            h[i] = Integer.parseInt(st.nextToken());
            w[i] = Integer.parseInt(st.nextToken());
            k[i] = Integer.parseInt(st.nextToken());
            kBefore[i] = k[i];
        }
        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());
            int idx = Integer.parseInt(st.nextToken());
            int dir = Integer.parseInt(st.nextToken());
            move(idx, dir);
        }
        int ans = 0;
        for (int i = 1; i <= N; i++) {
            if (k[i] > 0) {
                ans += (kBefore[i] - k[i]);
            }
        }
        System.out.println(ans);
    }

    private static void move(int idx, int dir) {
        if (k[idx] <= 0) {
            return;
        }
        // 현재 기사를 움직이는 경우, 밀리는 모든 기사들에 대해 위치 정보 갱신
        if (isPossible(idx, dir)) {
            for (int i = 1; i <= N; i++) {
                if (!isMoved[i]) {
                    continue;
                }
                r[i] = nextR[i];
                c[i] = nextC[i];
                k[i] -= dmg[i];
            }
        }
    }

    private static boolean isPossible(int idx, int dir) {
        for (int i = 1; i <= N; i++) {
            dmg[i] = 0;
            isMoved[i] = false;
            nextR[i] = 0;
            nextC[i] = 0;
        }
        Queue<Integer> q = new ArrayDeque<>();
        q.offer(idx);
        isMoved[idx] = true;
        while (!q.isEmpty()) {
            int now = q.poll();
            int nextX = r[now] + dx[dir];
            int nextY = c[now] + dy[dir];
            nextR[now] = nextX;
            nextC[now] = nextY;
            if (nextX < 1 || nextY < 1 || nextX + h[now] - 1 > L || nextY + w[now] - 1 > L) {
                continue;
            }
            // 현재 기사가 움직이면 장애물, 벽에 충돌하는지 체크
            for (int i = nextX; i <= nextX + h[now] - 1; i++) {
                for (int j = nextY; j <= nextY + w[now] - 1; j++) {
                    if (arr[i][j] == 1) {
                        dmg[now]++;
                    } else if (arr[i][j] == 2) {
                        return false;
                    }
                }
            }
            for (int i = 1; i <= N; i++) {
                if (isMoved[i]) {
                    continue;
                }
                if (k[i] <= 0) {
                    continue;
                }
                if (r[i] > nextX + h[now] - 1 || nextX > r[i] + h[i] - 1) {
                    continue;
                }
                if (c[i] > nextY + w[now] - 1 || nextY > c[i] + w[i] - 1) {
                    continue;
                }
                // 밀리는 기사 저장
                isMoved[i] = true;
                q.offer(i);
            }
        }
        dmg[idx] = 0;
        return true;
    }

}