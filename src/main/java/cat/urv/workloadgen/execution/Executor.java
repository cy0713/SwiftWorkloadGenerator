package cat.urv.workloadgen.execution;

import static cat.urv.config.WorkloadConfig.container;

/**
 * Execute here your workload model
 *
 */
public class Executor {
    public static void main( String[] args ){	
    	System.out.println("--------- Starting Workload!! -----------");
    	WorkloadExecutor executor = new IdiadaTraceWorkloadExecutor(); //ArcturTraceWorkloadExecutor(); // SinusoidalWorkloadExecutor();
    	executor.executeWorkload(container);
    	System.out.println("--------- Finishing Workload!! -----------");
    	System.exit(0);
    }
}
