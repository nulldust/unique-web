/**
 * Copyright (c) 2014-2015, biezhi 王爵 (biezhi.me@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.unique.web.server;

//import org.eclipse.jetty.server.Connector;
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.server.nio.SelectChannelConnector;
//import org.eclipse.jetty.servlet.DefaultServlet;
//import org.eclipse.jetty.servlet.ServletContextHandler;
//import org.eclipse.jetty.util.thread.QueuedThreadPool;
//import org.eclipse.jetty.webapp.WebAppContext;

/**
 * unique自带的web服务
 * @author biezhi
 * @since 1.0
 */
public class UniqueServer {

//    private static final Logger logger = LoggerFactory.getLogger(UniqueServer.class);
//
//    private int port = 9090;
//    
//    private String contextPath = "/";
//
//    private String webPath = "./WebContent";
//
//    private int threadNum = 50;
//
//    private Server server;
//
//    public UniqueServer init(Integer port, String webContext) {
//
//        this.port = (null != port) ? port : this.port;
//        this.webPath = (null != webContext) ? webContext : this.webPath;
//
//        server = new Server();
//
//        // 连接池
//        SelectChannelConnector connector = new SelectChannelConnector();
//        connector.setPort(port);
//        connector.setMaxIdleTime(30000);
//        connector.setRequestHeaderSize(8192);
//        QueuedThreadPool threadPool = new QueuedThreadPool(threadNum);
//        threadPool.setName("unique-jetty-http");
//        connector.setThreadPool(threadPool);
//
//        server.setConnectors(new Connector[] { connector });
//        ServletContextHandler context = null;
//        // webapp 可以支持 jsp
//        if (webPath != null) {
//            logger.info("load webPath={}", webPath);
//            try {
//                URL warUrl = new File(webPath).toURI().toURL();
//                final String warUrlString = warUrl.toExternalForm();
//                context = new WebAppContext(warUrlString, contextPath);
//                server.setHandler(context);
//            } catch (MalformedURLException e) {
//                logger.error(e.getMessage());
//            }
//        } else {
//            context = new ServletContextHandler(server, contextPath);
//        }
//
//        if (webPath == null) {
//            context.addServlet(DefaultServlet.class, "/*");
//        }
//        return this;
//    }
//    
//    /**
//     * start server
//     */
//    public void start() {
//        Runtime.getRuntime().addShutdownHook(new Thread() {
//            @Override
//            public void run() {
//                try {
//                    server.stop();
//                } catch (Exception e) {
//                    logger.error("run main stop error!", e);
//                }
//            }
//
//        });
//        try {
//            server.start();
//        } catch (Exception e) {
//            logger.warn("start has exception:", e);
//            System.exit(-1);
//        }
//        logger.info("jetty unique server started, port={}", port);
//    }
//
//    /**
//     * stop server
//     */
//    public void stop() {
//        try {
//            server.stop();
//            server.destroy();
//        } catch (Exception e) {
//            logger.warn("stop has exception:", e);
//            System.exit(-1);
//        }
//    }
    
}