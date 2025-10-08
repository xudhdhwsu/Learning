package monte;

import java.util.ArrayList;
import java.util.Random;

public class Molecule {
    
    /* 状态 */
    private MolecularState state;

    /* 当前时间 */
    // 注：设定时间的单位是ns
    private double currentTime = 0.0;
    private ArrayList<Double> photonTimes;

    private Random random;

    /* 分子基本现状 */
    private final double F1;
    private final double tau1;
    private final double tau2;
    private final double h;

    // /* 额外添加的过程 */
    private final double S1_to_T1;

    /* 默认参数 */
    // 默认参数具体值给多少我还不知到
    public Molecule() {
        this.photonTimes = new ArrayList<Double>();
        this.state = MolecularState.GROUND_STATE;
        this.random = new Random();
        this.F1 = 1;
        // 查询知乎
        this.tau1 = 5;
        this.tau2 = 5.25 * 1000;
        this.h = 0.9;

        // /* ---- */
        this.S1_to_T1 = 10;
    }

    /* 用户自己设定参数 */
    public Molecule(double F1, double tau1, double tau2, double h, double S1_to_T1) {
        this.photonTimes = new ArrayList<Double>();
        this.state = MolecularState.GROUND_STATE;
        this.random = new Random();
        this.F1 = F1;
        this.tau1 = tau1;
        this.tau2 = tau2;
        this.h = h;

        // /* ---- */
        this.S1_to_T1 = S1_to_T1;
    }
    
    public enum MolecularState {
        GROUND_STATE,      // 基态 S₀
        EXCITED_STATE,     // 激发态 S₁  
        TRIPLET_STATE      // 三线态 T₁
    }

    /* 获取当前状态 */
    public MolecularState getState() {
        return state;
    }
    
    /* 设定当前状态 */
    public void setState(MolecularState state) {
        this.state = state;
    }
    
    /* 获取现在时间 */
    public double getCurrentTime() {
        return currentTime;
    }

    /* 获取光子发射时间列表 */
    public ArrayList<Double> getPhotonTimes() {
        return photonTimes;
    }

    /* 生存函数的反函数，生成下一事件发生的时间 */
    public double sampleExponential(double rate) {
        // if (random.nextDouble() == 0.0) {
            // System.err.println("false");
        // }
        return -Math.log(1 - random.nextDouble()) / rate;
    }


    public void simulate(double maxTime) {
        while(currentTime < maxTime) {
            /* 状态判定 */
            switch(state) {
                /* 基态 */
                case GROUND_STATE:
                    double t0 = sampleExponential(F1);
                    currentTime += t0;
                    setState(MolecularState.EXCITED_STATE);
                    // System.out.println("当前时间：" + currentTime + " ns, 分子状态： " + getState());
                    // break;
                /* 激发态 */
                case EXCITED_STATE:
                    double r = random.nextDouble();
                    /* 如果 r <= h 则认定其发射光子，否则认定跃迁至三线态 */
                    if (r <= h) {
                        double t1 = sampleExponential(1 / tau1);
                        currentTime += t1;
                        /* 设定为停留时间结束后才发出光子 */
                        photonTimes.add(currentTime);
                        setState(MolecularState.GROUND_STATE);
                        // System.out.println("当前时间：" + currentTime + " ns, 分子状态： " + getState() + "，发射光子！" );
                    }
                    else {
                        // /* ok，这里存在一个问题,因为不可能时间不动，它就到三线态了 */
                        // /* ---- */
                        double t2 = sampleExponential(1 / S1_to_T1);
                        currentTime += t2;
                        setState(MolecularState.TRIPLET_STATE);
                        // System.out.println("当前时间：" + currentTime + " ns, 分子状态： " + getState());
                    }
                    // break;

                /* 三线态 */
                case TRIPLET_STATE:
                    double t3 = sampleExponential(1 / tau2);
                    currentTime += t3;
                    setState(MolecularState.GROUND_STATE);
                    // System.out.println("当前时间：" + currentTime + " ns, 分子状态： " + getState());
                    // break;
            }
        }
    }


    /*  */
    public static void main(String[] args) {
        Molecule molecule = new Molecule();
        long stime = System.currentTimeMillis();
        /* 先来一秒钟尝尝咸淡 */
        molecule.simulate(300 * Math.pow(10, 9));
        long etime = System.currentTimeMillis();
        // 计算执行时间
        System.out.printf("执行时长：%d 毫秒.", (etime - stime));
    }
    
}
