package monte;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultipleMoleculesSimulation {
    
    private List<Molecule> molecules;
    private double simulationTime;

    private List<Integer> photonCounts;
    
    public MultipleMoleculesSimulation(int numMolecules, double simulationTime) {
        this.molecules = new ArrayList<>();
        this.simulationTime = simulationTime;
        
        /* 使用默认参数创建分子 */
        for (int i = 0; i < numMolecules; i++) {
            molecules.add(new Molecule());
        }
    }
    
    public MultipleMoleculesSimulation(int numMolecules, double simulationTime, double F1, double tau1, double tau2, double h, double S1_to_T1) {
        this.molecules = new ArrayList<>();
        this.simulationTime = simulationTime;
        
        /* 使用自定义参数创建分子 */
        for (int i = 0; i < numMolecules; i++) {
            molecules.add(new Molecule(F1, tau1, tau2, h, S1_to_T1));
        }
    }
    
    public void simulateParallel() {
        System.out.println("开始模拟 " + molecules.size() + " 个分子");
        
        /*  */
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < molecules.size(); i++) {
            /* 但是这种一个一个的跑有一点难受啊,怎么改呢? 让我搜搜多线程*/
            molecules.get(i).simulate(simulationTime);
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("模拟完成，耗时: " + (endTime - startTime) + " ms");
    }

    
    public void getPhotonCounts(double simulationTime , int step) {

        List<Double> allPhotonTimes = new ArrayList<>();
        for (Molecule molecule : molecules) {
            allPhotonTimes.addAll(molecule.getPhotonTimes());
        }
        
        Collections.sort(allPhotonTimes);

        // 初始化光子计数数组
        this.photonCounts = new ArrayList<>();
        // Exception in thread "main" java.lang.OutOfMemoryError: Requested array size exceeds VM limit
        /* ok 啊,除以 10 就解决了 */

        // 初始化
        for (int i = 0; i < simulationTime / step; i++) {
            photonCounts.add(0);
        }
        
        // 统计每10纳秒内的光子数量
        for (double time : allPhotonTimes) {
            int index = (int)(time / step);
            photonCounts.set(index, photonCounts.get(index) + 1);
        }

    }
    
    public static void main(String[] args) {
        /* 多个分子 */
        int numMolecules = 10;
        double simulationTime = 3 * Math.pow(10, 9)/* 秒 */;
        
        MultipleMoleculesSimulation simulation = new MultipleMoleculesSimulation(numMolecules, simulationTime);
        
        simulation.simulateParallel();
        
        simulation.getPhotonCounts(simulationTime, 10000);  // 每1毫秒一个时间段

        for (int i = 0; i < simulation.photonCounts.size(); i++) {
            System.out.println("时间 " + (i) + " ms - " + (i + 1 ) + " ms: " + simulation.photonCounts.get(i) + " 个光子");
        }

    }
}