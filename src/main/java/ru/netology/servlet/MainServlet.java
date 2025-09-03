package ru.netology.servlet;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class MainServlet extends HttpServlet {
  // контроллер, который будет управлять запросами и делегировать их в сервис
  private PostController controller;
  @Override
// инициализация сервлета: создаем репозиторий, сервис и контроллер
  public void init() {
    final var repository = new PostRepository(); // создаем репозиторий
    final var service = new PostService(repository); // создаем сервис

    controller = new PostController(service); // создаем контроллер
  }
  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      if (method.equals("GET") && path.equals("/api/posts")) {
        controller.all(resp); // запрос для получения всех постов
        return;
      }
      if (method.equals("GET") && path.matches("/api/posts/\\d+")) {
      // запрос для получения поста по id
        final var id = Long.parseLong(path.substring(path.lastIndexOf("/"))); // извлекаем id из url
        controller.getById(id, resp);
        return;
      }
      if (method.equals("POST") && path.equals("/api/posts")) {
        controller.save(req.getReader(), resp); // запрос на создание/обновление поста
        return;
      }
      if (method.equals("DELETE") && path.matches("/api/posts/\\d+")) {
        final var id = Long.parseLong(path.substring(path.lastIndexOf("/"))); // извлекаем id из url
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace(); // печатаем ошибку в случае исключений
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // возвращаем ошибку
    }
  }
}

