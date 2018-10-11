package controllers

import javax.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.Forms.{longNumber, mapping, optional}
import play.api.mvc._
import play.core.Paths
import views.html

import scala.concurrent.ExecutionContext

class FilesController @Inject()(cc: MessagesControllerComponents)
                               (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  /**
    *
    */
  def index = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def add = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.upload(""))
  }

//  def del: Action[AnyContent] = Action { implicit request =>
//    //    val idValue = fileForm.bindFromRequest().get
//    //    println(idValue)
//    Ok(html.del(fileForm))
//  }

  /**
    *
    */
  def del: Action[AnyContent] = Action { implicit request =>
    fileForm.bindFromRequest().fold(
      formWithErrors => {
        BadRequest(views.html.del(formWithErrors))
      },
      userData => {
        println("Id=" + userData.id)
        val upFile = UpFile(userData.id, Some(""))
        Home
      }
    )
  }


  val Home: Result = Redirect(routes.FilesController.index())

  def delete(id: Long) = Action { implicit request: Request[AnyContent] =>
    val idValue = fileForm.bindFromRequest().get
    println(idValue)
    Ok("File deleted")
  }

  val fileForm = Form(
    mapping(
      "id" -> longNumber,
      "path" -> optional(text)
    )(UpFile.apply)(UpFile.unapply)
  )


  /**
    *
    */
  def download(id: Long): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  /**
    *
    */
  def link(id: Long, expirationTs: Long): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  /**
    *
    */
  def upload = Action(parse.multipartFormData) { request =>
    request.body.file("userFile").map { picture =>

      println(picture.filename)
      Ok("File uploaded")
    }.getOrElse {
      Redirect(routes.FilesController.index).flashing(
        "error" -> "Missing file")
    }
  }

}

case class UpFile(id: Long, path: Option[String])