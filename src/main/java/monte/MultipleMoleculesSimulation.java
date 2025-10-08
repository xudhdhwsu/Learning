package monte;

import java.util.ArrayList;
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
            /* 但是这种一个一个的跑有一点难受啊,怎么改呢? */
            molecules.get(i).simulate(simulationTime);
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("模拟完成，耗时: " + (endTime - startTime) + " ms");
    }


    public static void main(String[] args) {
        // 测试多分子模拟
        int numMolecules = 100;
        double simulationTime = 1000.0; // 1000 ns
        
        MultipleMoleculesSimulation simulation = new MultipleMoleculesSimulation(numMolecules, simulationTime);
        
        simulation.simulateParallel();
        
    }
}