package shareniu.ch19;

import org.camunda.bpm.engine.form.StartFormData;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.engine.JuelFormEngine;

public class ShareniuFormEngine extends JuelFormEngine {

    public String getName() {
        return "shareniu";
    }
    public Object renderStartForm(StartFormData startForm) {
      return super.renderStartForm(startForm);
    }
    public Object renderTaskForm(TaskFormData taskForm) {
       return  super.renderTaskForm(taskForm);
    }
}
