<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<html>
<head><title>流浪动物列表</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/style.css">
<script src="<%=request.getContextPath()%>/static/bubbles.js"></script>
<script>window.__ctx='<%=request.getContextPath()%>';</script>
<script src="<%=request.getContextPath()%>/static/nav-avatar.js"></script>
</head>
<body>
<div class="navbar">
  <div class="links">
    <a href="<%=request.getContextPath()%>/">用户主页</a>
    <a href="<%=request.getContextPath()%>/animals">流浪动物</a>
    <a href="<%=request.getContextPath()%>/forum">论坛</a>
    <a href="<%=request.getContextPath()%>/knowledge">护养知识</a>
    <a href="<%=request.getContextPath()%>/user/animals" style="margin-left:auto">添加流浪动物</a>
    <a href="<%=request.getContextPath()%>/user">个人中心</a>
    <a href="<%=request.getContextPath()%>/admin">管理</a>
  </div>
  <div class="right">
    <% if (session.getAttribute("userId") == null) { %>
      <a href="<%=request.getContextPath()%>/login">登录</a>
      <a href="<%=request.getContextPath()%>/register">注册</a>
      <a href="<%=request.getContextPath()%>/login?role=admin">管理员登录</a>
    <% } else { %>
      <% String av = (String)session.getAttribute("avatarUrl"); String avatarSrc = null; if (av != null && !av.isEmpty()) { avatarSrc = (av.startsWith("http://")||av.startsWith("https://")||av.startsWith("/"))?av:(request.getContextPath()+"/"+av); } else { avatarSrc = request.getContextPath()+"/static/avatar.png"; } %>
      <div class="nav-avatar" data-user-url="<%=request.getContextPath()%>/user">
        <a href="#"><img src="<%=avatarSrc%>" class="avatar-sm" onerror="this.src='<%=request.getContextPath()%>/static/avatar.png'"/></a>
        <div class="logout-pop"><a href="<%=request.getContextPath()%>/user">个人中心</a><a href="<%=request.getContextPath()%>/logout" style="margin-left:8px">退出登录</a></div>
      </div>
    <% } %>
  </div>
</div>
<div class="pageI">
<h3>流浪动物列表</h3>
<% List<Map<String,Object>> animals = (List<Map<String,Object>>)request.getAttribute("animals"); %>
<div>
<% if (animals != null) for (Map<String,Object> a: animals) { %>
  <div class="card" style="margin:8px;display:inline-block;width:280px;vertical-align:top;">
    <div>
      <% String p = (String)a.get("photo_url"); String src = (p!=null && (p.startsWith("http://")||p.startsWith("https://")||p.startsWith("/"))) ? p : (p==null?"":(request.getContextPath()+"/"+p)); %>
      <img src="<%=src%>" alt="photo" style="max-width:260px;max-height:160px" onerror="this.style.display='none'"/>
    </div>
    <div>
      <b><%=a.get("name")%></b> / <%=a.get("species")%> / <%=a.get("breed")%>
    </div>
    <div>
      年龄：<%=a.get("age")%> 健康：<%=a.get("health_status")%>
    </div>
    <div>
      <a href="<%=request.getContextPath()%>/animals?id=<%=a.get("id")%>">查看详情</a>
    </div>
  </div>
<% } %>
</div>
</div>
</body>
</html>
