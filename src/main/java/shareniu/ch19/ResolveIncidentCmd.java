package shareniu.ch19;

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.incident.IncidentContext;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.runtime.Incident;

public class ResolveIncidentCmd implements Command<Void> {

    Incident  incident;
    public ResolveIncidentCmd(Incident incident) {
        this.incident = incident;
    }
    /**
     * delete from ACT_RU_INCIDENT where ID_ = ? and REV_ = ?	Update counts: [1]
     * Result 1:	update ACT_RU_EXECUTION set REV_ = ?, PROC_DEF_ID_ = ?, BUSINESS_KEY_ = ?, ACT_ID_ =
     * @param commandContext
     * @return
     */
    public Void execute(CommandContext commandContext) {
        IncidentContext incidentContext=new IncidentContext(incident);
        ProcessEngineConfigurationImpl processEngineConfiguration = commandContext.getProcessEngineConfiguration();
        processEngineConfiguration.getIncidentHandlers().get(incident.getIncidentType())
            .resolveIncident(incidentContext);
        return null;
    }
}
