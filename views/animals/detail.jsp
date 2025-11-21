<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<html>
<head><title>动物详情</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/style.css">
<script src="<%=request.getContextPath()%>/static/bubbles.js"></script>
<script>window.__ctx='<%=request.getContextPath()%>';</script>
<script src="<%=request.getContextPath()%>/static/nav-avatar.js"></script>
</head>
<body>
<% boolean isAdmin = "admin".equals(request.getParameter("from")); boolean readOnly = isAdmin || "readonly".equals(request.getParameter("from")); %>
<% if (isAdmin) { %>
<div class="navbar">
  <div class="links">
    <a href="<%=request.getContextPath()%>/">用户主页</a>
    <a href="<%=request.getContextPath()%>/animals">流浪动物信息</a>
    <a href="<%=request.getContextPath()%>/user/animals" style="margin-left:auto">添加流浪动物</a>
    <a href="<%=request.getContextPath()%>/forum">论坛交流</a>
    <a href="<%=request.getContextPath()%>/knowledge">护养知识</a>
    <a href="<%=request.getContextPath()%>/user">个人中心</a>
    <a href="<%=request.getContextPath()%>/admin">管理</a>
  </div>
  <div class="right"><a href="<%=request.getContextPath()%>/logout">退出</a></div>
</div>
<% } else { %>
<div class="navbar">
  <div class="links">
    <a href="<%=request.getContextPath()%>/">用户主页</a>
    <a href="<%=request.getContextPath()%>/animals">流浪动物</a>
    <a href="<%=request.getContextPath()%>/forum">论坛</a>
    <a href="<%=request.getContextPath()%>/knowledge">护养知识</a>
    <a href="<%=request.getContextPath()%>/user/animals" style="margin-left:auto">添加流浪动物</a>
    <a href="<%=request.getContextPath()%>/user">个人中心</a>
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
<% } %>
<% Map<String,Object> a = (Map<String,Object>)request.getAttribute("animal"); %>
<div class="page">
<h3><%=a.get("name")%></h3>
<% String p = (String)a.get("photo_url"); String src = (p!=null && (p.startsWith("http://")||p.startsWith("https://")||p.startsWith("/"))) ? p : (p==null?"":(request.getContextPath()+"/"+p)); %>
<img src="<%=src%>" style="max-width:400px;" onerror="this.onerror=null;this.src='<%=request.getContextPath()%>/static/avatar.png'"/>
<div>品种：<%=a.get("species")%> / <%=a.get("breed")%></div>
<div>年龄：<%=a.get("age")%></div>
<div>健康：<%=a.get("health_status")%></div>
<div>救助地点：<%=a.get("rescue_location")%></div>
<div>救助时间：<%=a.get("rescue_time")%></div>
<div>描述：<%=a.get("description")%></div>
<hr/>
<%
  Object ad = a.get("adopted");
  boolean adopted = ad instanceof Boolean ? (Boolean) ad : ("1".equals(String.valueOf(ad)));
  boolean showForm = "1".equals(request.getParameter("apply"));
%>
<% if (readOnly) { %>
  <% if (adopted) { %>
    <div style="color:#ff4d88">该动物已被领养</div>
  <% } else { %>
    <div>该动物未被领养</div>
  <% } %>
<% } else { %>
  <% if (adopted) { %>
    <div style="color:#ff4d88">该动物已被领养</div>
  <% } else { %>
    <% if (!showForm) { %>
      <a class="card" href="<%=request.getContextPath()%>/animals?id=<%=a.get("id")%>&apply=1" style="display:inline-block;padding:8px">申请领养</a>
    <% } else { %>
      <form method="post" action="<%=request.getContextPath()%>/adoption">
        <input type="hidden" name="animalId" value="<%=a.get("id")%>"/>
        <label>居住环境：<textarea name="residence" required></textarea></label><br/>
        <label>饲养经验：<textarea name="experience" required></textarea></label><br/>
        <label>备注：<textarea name="message" required></textarea></label><br/>
        <button type="submit">提交领养申请</button>
        <% String err = (String)request.getAttribute("error"); if (err != null) { %>
          <div style="color:#ff4d88"><%=err%></div>
        <% } %>
      </form>
    <% } %>
  <% } %>
<% } %>
</div>
</body>
</html>
