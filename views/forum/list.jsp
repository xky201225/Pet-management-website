<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<html>
<head><title>论坛交流</title>
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
<h3>论坛话题</h3>
<form method="get" action="">
  <% String cat = request.getParameter("category"); %>
  <label>分类</label>
    <select name="category" required>
      <option value="求助" <%= "求助".equals(cat)?"selected":"" %>>求助</option>
      <option value="护养" <%= "护养".equals(cat)?"selected":"" %>>护养</option>
      <option value="寻找" <%= "寻找".equals(cat)?"selected":"" %>>寻找</option>
      <option value="领养故事" <%= "领养故事".equals(cat)?"selected":"" %>>领养故事</option>
      <option value="经验分享" <%= "经验分享".equals(cat)?"selected":"" %>>经验分享</option>
    </select>
  <label>关键字：<input name="keyword" value="<%=request.getParameter("keyword")==null?"":request.getParameter("keyword")%>"/></label>
  <button type="submit">检索</button>
  <a href="#" onclick="document.getElementById('create').style.display='block'">发布话题</a>
 </form>
 <div id="create" style="display:none">
   <form method="post" action="<%=request.getContextPath()%>/forum">
     <input type="hidden" name="action" value="create"/>
     <div class="form-row"><label>标题</label><input name="title" required/></div>
     <div class="form-row"><label>分类</label>
       <select name="category" required>
         <option value="求助">求助</option>
         <option value="护养">护养</option>
         <option value="寻找">寻找</option>
         <option value="领养故事">领养故事</option>
         <option value="经验分享">经验分享</option>
       </select>
     </div>
     <div class="form-row"><label>内容</label><textarea name="content" required></textarea></div>
     <div class="form-actions"><button type="submit">发布</button></div>
   </form>
 </div>
<% List<Map<String,Object>> topics = (List<Map<String,Object>>)request.getAttribute("topics"); %>
<% java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm"); %>
<div>
<% if (topics != null) for (Map<String,Object> t: topics) { %>
  <div class="card" style="margin:8px;">
    <div style="display:flex;align-items:center;gap:10px">
      <% String av = (String)t.get("avatar_url"); String avSrc = (av!=null && (av.startsWith("http://")||av.startsWith("https://")||av.startsWith("/")))?av:(av==null?"":(request.getContextPath()+"/"+av)); %>
      <a href="<%=request.getContextPath()%>/user/profile?id=<%=t.get("user_id")%>"><img src="<%=avSrc%>" class="avatar avatar-sm" onerror="this.src='<%=request.getContextPath()%>/static/avatar.png'"/></a>
      <div>
        <div><b><%=t.get("username")%></b> · <a href="<%=request.getContextPath()%>/forum?id=<%=t.get("id")%>"><%=t.get("title")%></a> · <small><%=fmt.format((java.util.Date)t.get("created_at"))%></small></div>
      </div>
    </div>
  </div>
<% } %>
</div>
</div>
</body>
</html>
