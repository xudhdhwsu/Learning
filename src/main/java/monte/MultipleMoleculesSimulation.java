package monte;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultipleMoleculesSimulation {
    
    private List<Molecule> molecules;
    private double simulationTime;
    
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

    public List<Integer> getPhotonCounts(double timeStep) {
        // 收集所有分子的发光时间
        List<Double> allPhotonTimes = new ArrayList<>();
        for (Molecule molecule : molecules) {
            allPhotonTimes.addAll(molecule.getPhotonTimes());
        }
        
        Collections.sort(allPhotonTimes);
        
        // 计算时间范围
        double minTime = allPhotonTimes.get(0);
        double maxTime = allPhotonTimes.get(allPhotonTimes.size() - 1);
        
        // 计算需要的时间段数量
        int numBins = (int) Math.ceil((maxTime - minTime) / timeStep) + 1;
        
        // 初始化光子计数数组
        List<Integer> photonCounts = new ArrayList<>(numBins);
        for (int i = 0; i < numBins; i++) {
            photonCounts.add(0);
        }
        
        // 统计每个时间段的光子数
        for (double photonTime : allPhotonTimes) {
            int binIndex = (int) ((photonTime - minTime) / timeStep);
            if (binIndex < numBins) {
                photonCounts.set(binIndex, photonCounts.get(binIndex) + 1);
            }
        }
        
        return photonCounts;
    }
    
    public static void main(String[] args) {
        /* 多个分子 */
        int numMolecules = 100;
        double simulationTime = 300 * Math.pow(10, 9)/* 秒 */;
        
        MultipleMoleculesSimulation simulation = new MultipleMoleculesSimulation(numMolecules, simulationTime);
        
        simulation.simulateParallel();
        
    }
}