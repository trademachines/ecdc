package ecdc.api.notifiers

import com.amazonaws.services.cloudwatchevents.model.{PutEventsRequest, PutEventsRequestEntry}
import com.amazonaws.services.ecs.model.TaskDefinition
import model.{Cluster, Service, Version}
import play.api.libs.json.Json

import scala.concurrent.Future

trait DeploymentNotifier {
  def success(cluster: Cluster, service: Service, version: Version, taskDef: TaskDefinition): Future[Any]
}

case class CloudWatchEventsNotifier(eventsClient: CloudWatchEventsClient) extends DeploymentNotifier {
  def success(cluster: Cluster, service: Service, version: Version, taskDef: TaskDefinition): Future[Any] = {
    val deployment = Json.obj(
      "cluster" -> cluster.name,
      "service" -> service.name,
      "version" -> version.value,
      "taskDefinitionArn" -> taskDef.getTaskDefinitionArn,
      "taskDefinitionRevision" -> taskDef.getRevision.toString
    ).toString

    val event = new PutEventsRequestEntry()
      .withSource("tm.ecdc")
      .withDetailType("ECDC Service Deployment Started")
      .withDetail(deployment)

    eventsClient.putEvents(new PutEventsRequest().withEntries(event))
  }
}
