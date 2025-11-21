<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<html>
<head><title>护养知识</title>
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
      <% String navAv = (String)session.getAttribute("avatarUrl"); String avatarSrc = null; if (navAv != null && !navAv.isEmpty()) { avatarSrc = (navAv.startsWith("http://")||navAv.startsWith("https://")||navAv.startsWith("/"))?navAv:(request.getContextPath()+"/"+navAv); } else { avatarSrc = request.getContextPath()+"/static/avatar.png"; } %>
      <div class="nav-avatar" data-user-url="<%=request.getContextPath()%>/user">
        <a href="#"><img src="<%=avatarSrc%>" class="avatar-sm" onerror="this.src='<%=request.getContextPath()%>/static/avatar.png'"/></a>
        <div class="logout-pop"><a href="<%=request.getContextPath()%>/user">个人中心</a><a href="<%=request.getContextPath()%>/logout" style="margin-left:8px">退出登录</a></div>
      </div>
    <% } %>
  </div>
</div>
<div class="page">
<h3>护养知识</h3>
<div style="margin-bottom:8px"><a href="#" onclick="document.getElementById('publish').style.display='block'">发布文章</a></div>
<div id="publish" style="display:none" class="card" >
  <form method="post" action="<%=request.getContextPath()%>/knowledge">
    <input type="hidden" name="action" value="publish"/>
    <div class="form-row"><label>标题</label><input name="title" required/></div>
    <div class="form-row"><label>分类</label><input name="category" required/></div>
    <div class="form-row"><label>内容</label><textarea name="content" required></textarea></div>
    <div class="form-actions"><button type="submit">发布</button></div>
  </form>
 </div>
<form method="get" action="">
  <label>分类：<input name="category" value="<%=request.getParameter("category")==null?"":request.getParameter("category")%>"/></label>
  <label>关键字：<input name="keyword" value="<%=request.getParameter("keyword")==null?"":request.getParameter("keyword")%>"/></label>
  <button type="submit">检索</button>
 </form>
<% List<Map<String,Object>> articles = (List<Map<String,Object>>)request.getAttribute("articles"); java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm"); %>
<div>
  <% if (articles != null) for (Map<String,Object> a: articles) { %>
    <div class="card" style="margin:8px;">
      <div style="display:flex;align-items:center;gap:10px">
        <% String av = (String)a.get("avatar_url"); String avSrc = (av!=null && (av.startsWith("http://")||av.startsWith("https://")||av.startsWith("/")))?av:(av==null?"":(request.getContextPath()+"/"+av)); %>
        <a href="<%=request.getContextPath()%>/user/profile?id=<%=a.get("user_id")%>"><img src="<%=avSrc%>" class="avatar avatar-sm" onerror="this.src='<%=request.getContextPath()%>/static/avatar.png'"/></a>
        <div>
          <div><b><%=a.get("username")!=null?a.get("username"):a.get("author")%></b> · <a href="<%=request.getContextPath()%>/knowledge?id=<%=a.get("id")%>"><%=a.get("title")%></a> · <small><%=fmt.format((java.util.Date)a.get("published_at"))%></small></div>
        </div>
      </div>
    </div>
  <% } %>
  <% if (articles == null || articles.isEmpty()) { %>
    <div class="card">暂无文章</div>
  <% } %>
</div>
</div>
</body>
</html>
