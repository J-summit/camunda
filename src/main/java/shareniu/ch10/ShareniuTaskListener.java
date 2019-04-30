package shareniu.ch10;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

public class ShareniuTaskListener implements TaskListener {

    public void notify(DelegateTask delegateTask) {
        System.out.println("#################");
        String eventName = delegateTask.getEventName();
        System.out.println("eventName:"+eventName);
        int priority = delegateTask.getPriority();
        System.out.println(priority);
        if (eventName.equals("create")){
            delegateTask.addCandidateUser("张三丰");
        }
        System.out.println("#################");
    }
}
