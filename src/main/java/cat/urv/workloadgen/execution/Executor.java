package cat.urv.workloadgen.execution;

import static cat.urv.config.WorkloadConfig.container;

/**
 * Hello world!
 *
 */
public class Executor {
    public static void main( String[] args ){	
    	System.out.println("--------- Starting Workload!! -----------");
    	WorkloadExecutor executor = new ArcturTraceWorkloadExecutor(); // SinusoidalWorkloadExecutor();
    	executor.executeWorkload(container);
    	System.out.println("--------- Finishing Workload!! -----------");
    	System.exit(0);
    }
}
