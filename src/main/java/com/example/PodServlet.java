package com.example;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/")
public class PodServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        try {
            ApiClient client = Config.defaultClient();
            CoreV1Api api = new CoreV1Api(client);

            V1PodList podList = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
            response.getWriter().println("<h1>Kubernetes Pods</h1>");
            response.getWriter().println("<ul>");

            for (V1Pod pod : podList.getItems()) {
                response.getWriter().println("<li>" + pod.getMetadata().getName() + " (" + pod.getStatus().getPhase() + ")</li>");
            }

            response.getWriter().println("</ul>");
        } catch (Exception e) {
            response.getWriter().println("<p>Error fetching pod details: " + e.getMessage() + "</p>");
        }
    }
}