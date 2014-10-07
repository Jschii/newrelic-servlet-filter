package yleisradio.newrelic

import org.scalatra.{RouteTransformer, Route, ScalatraServlet}
import com.newrelic.api.agent.{Trace, NewRelic}
import scala.util.Try

trait NewRelicTracingScalatraServlet { servlet: ScalatraServlet =>

  private lazy val servletRootPath = Try { getServletRootPath }.toOption.getOrElse("")

  servlet.error {
    case e: Throwable => {
        NewRelic.noticeError(e)
        throw e
    }
  }

  def get(route: String)(action: => Any): Route =
    servlet.get(transform(route))(traceAction(route, action))

  def post(route: String)(action: => Any): Route =
    servlet.post(transform(route))(traceAction(route, action))

  def put(route: String)(action: => Any): Route =
    servlet.put(transform(route))(traceAction(route, action))

  def delete(route: String)(action: => Any): Route =
    servlet.delete(transform(route))(traceAction(route, action))

  def patch(route: String)(action: => Any): Route =
    servlet.patch(transform(route))(traceAction(route, action))

  def options(route: String)(action: => Any): Route =
    servlet.options(transform(route))(traceAction(route, action))


  private def transform(route: String): RouteTransformer =
    route

  @Trace(dispatcher = true)
  private def traceAction(route: String, action: => Any): Any = {
    def traceRequest() {
      val path = servletRootPath + (if (route.startsWith("/")) route else "/" + route)
      NewRelic.setTransactionName("web", path)
      NewRelic.setRequestAndResponse(NewRelicRequest(request), NewRelicResponse(response))

      params.foreach { case (key, value) => NewRelic.addCustomParameter(key, value) }
    }

    Try { traceRequest() }
    action
  }

  private def getServletRootPath = {
    def untrail(path: String) =
      path.replaceAll("/+\\*$", "")

    val ctxPath = servlet.getServletContext.getContextPath
    val servletPath = servlet.getServletContext.getServletRegistration(getClass.getName)
      .getMappings.iterator().next()
    untrail(ctxPath) + untrail(servletPath)
  }

}