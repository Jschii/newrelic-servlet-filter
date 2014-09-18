package yleisradio.newrelic

import com.newrelic.api.agent.{NewRelic,Trace,Request,Response,HeaderType}
import javax.servlet._
import javax.servlet.http._

class NewRelicTracingFilter extends Filter {
  def init(cfg: FilterConfig) {

  }

  def destroy() {

  }

  def doFilter(request: ServletRequest, response: ServletResponse, filterChain: FilterChain) {
    traceRequest(request.asInstanceOf[HttpServletRequest], response.asInstanceOf[HttpServletResponse]) {
      filterChain.doFilter(request, response)
    }
  }

  @Trace(dispatcher=true)
  def traceRequest(request: HttpServletRequest, response: HttpServletResponse)(block: => Any) = {
    val reportRequest = NewRelicRequest(request)
    val reportResponse = NewRelicResponse(response)

    NewRelic.setTransactionName("api", s"${reportRequest.getRequestURI} ${request.getMethod}")
    NewRelic.setRequestAndResponse(reportRequest, reportResponse)

    try {
      block
    } catch {
      case ex: Throwable => {
        NewRelic.noticeError(ex)
        throw ex
      }
    }
  }
}


