package ecdc.api.notifiers

import com.amazonaws.services.cloudwatchevents.AmazonCloudWatchEventsAsync
import com.amazonaws.services.cloudwatchevents.model.{ PutEventsRequest, PutEventsResult }

import scala.concurrent.Future

class CloudWatchEventsClient(amazonECSAsyncClient: AmazonCloudWatchEventsAsync) {
  def putEvents(putEventsRequest: PutEventsRequest): Future[PutEventsResult] = {
    wrapAsyncMethod(amazonECSAsyncClient.putEventsAsync, putEventsRequest)
  }
}
