package molecule.core.util
import datomic.ListenableFuture
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.control.NonFatal

trait BridgeDatomicFuture {

  private[molecule] def bridgeDatomicFuture[T](listenF: ListenableFuture[T])(implicit ec: ExecutionContext): Future[T] = {
    val p = Promise[T]()
    listenF.addListener(
      new java.lang.Runnable {
        override def run: Unit = {
          try {
            p.success(listenF.get())
          } catch {
            case e: java.util.concurrent.ExecutionException =>
              p.failure(e.getCause)
            case NonFatal(e)                                =>
              p.failure(e)
          }
          ()
        }
      },
      (arg0: Runnable) => ec.execute(arg0)
    )
    p.future
  }
}
