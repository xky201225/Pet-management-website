<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<html>
<head><title>个人中心</title>
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
    <% String navAv = (String)session.getAttribute("avatarUrl"); String navAvatarSrc = null; if (navAv != null && !navAv.isEmpty()) { navAvatarSrc = (navAv.startsWith("http://")||navAv.startsWith("https://")||navAv.startsWith("/"))?navAv:(request.getContextPath()+"/"+navAv); } else { navAvatarSrc = request.getContextPath()+"/static/avatar.png"; } %>
    <div class="nav-avatar" data-user-url="<%=request.getContextPath()%>/user">
      <a href="#"><img src="<%=navAvatarSrc%>" class="avatar-sm" onerror="this.src='<%=request.getContextPath()%>/static/avatar.png'"/></a>
      <div class="logout-pop"><a href="<%=request.getContextPath()%>/user">个人中心</a><a href="<%=request.getContextPath()%>/logout" style="margin-left:8px">退出登录</a></div>
    </div>
  </div>
</div>
<div class="page">
<h3>个人中心</h3>
<%
  String av = (String)request.getAttribute("avatarUrl");
  String avatarSrc = null;
  if (av != null && !av.isEmpty()) {
      if (av.startsWith("http://") || av.startsWith("https://") || av.startsWith("/")) avatarSrc = av;
      else avatarSrc = request.getContextPath() + "/" + av;
  } else {
      avatarSrc = request.getContextPath() + "/static/avatar.png";
  }
%>
<div>头像：<img src="<%=avatarSrc%>" class="avatar avatar-sm" onerror="this.src='<%=request.getContextPath()%>/static/avatar.png'"/></div>
    <br>
    <div>用户名：<%=session.getAttribute("username")%></div>
    <div style="display:flex;justify-content:space-between;align-items:center">
    <button onclick="document.getElementById('edit').style.display='block'">修改个人信息</button>
</div>
    <div id="edit" style="display:none" class="card">
    <form method="post" action="<%=request.getContextPath()%>/user/update" enctype="multipart/form-data">
        <div class="form-row"><label>头像</label><input type="file" name="avatar" accept="image/*"/></div>
        <div class="form-row"><label>用户名</label><input name="username" value="<%=session.getAttribute("username")%>"/></div>
        <div class="form-row"><label>联系方式</label><input name="phone"/></div>
        <div class="form-row"><label>原密码</label><input type="password" name="old_password"/></div>
        <div class="form-row"><label>新密码</label><input type="password" name="new_password"/></div>
        <div class="form-row"><label>确认新密码</label><input type="password" name="confirm_password"/></div>
        <div class="form-actions"><button type="submit">保存</button></div>
    </form>
    <% String error = (String)request.getAttribute("error"); if (error != null) { %>
    <div style="color:#ff4d88"><%=error%></div>
    <% } %>
</div>
<hr/>

<h4>收养申请办理进度</h4>
<% List<Map<String,Object>> apps = (List<Map<String,Object>>)request.getAttribute("applications"); java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm"); %>
 <table border="1" cellspacing="0" cellpadding="6">
   <tr><th>ID</th><th>动物</th><th>状态</th><th>处理状态</th><th>审核意见</th><th>提交时间</th></tr>
  <% if (apps != null) { %>
  <% for (Map<String,Object> m: apps) { String st = String.valueOf(m.get("status")); String ck = String.valueOf(m.get("check")); if ("未处理".equals(ck)) { %>
    <tr>
      <td><%=m.get("id")%></td>
      <td><a href="<%=request.getContextPath()%>/animals?id=<%=m.get("animal_id")%>&from=readonly"><%=m.get("animal_name")%></a></td>
      <td><span class="<%= "未批准".equals(st)?"status-bad":"批准".equals(st)?"status-ok":"" %>"><%=st%></span></td>
      <td><%=ck%></td>
      <td><%=m.get("review_opinion")%></td>
      <td><%=fmt.format((java.util.Date)m.get("created_at"))%></td>
    </tr>
  <% } } } %>
  <% if (apps != null) { %>
  <% for (Map<String,Object> m: apps) { String st = String.valueOf(m.get("status")); String ck = String.valueOf(m.get("check")); if ("已处理".equals(ck)) { %>
    <tr>
      <td><%=m.get("id")%></td>
      <td><a href="<%=request.getContextPath()%>/animals?id=<%=m.get("animal_id")%>&from=readonly"><%=m.get("animal_name")%></a></td>
      <td><span class="<%= "未批准".equals(st)?"status-bad":"批准".equals(st)?"status-ok":"" %>"><%=st%></span></td>
      <td><%=ck%></td>
      <td><%=m.get("review_opinion")%></td>
      <td><%=fmt.format((java.util.Date)m.get("created_at"))%></td>
    </tr>
  <% } } } %>
  <% if (apps == null || apps.isEmpty()) { %>
    <tr><td colspan="5">暂无申请</td></tr>
  <% } %>
 </table>
 <hr/>
 <h4>添加流浪动物申请办理进度</h4>
<% List<Map<String,Object>> subs = (List<Map<String,Object>>)request.getAttribute("submissions"); %>
 <table border="1" cellspacing="0" cellpadding="6">
   <tr><th>ID</th><th>名称</th><th>物种</th><th>状态</th><th>处理状态</th><th>提交时间</th></tr>
  <% if (subs != null) { %>
  <% for (Map<String,Object> s: subs) { String st2 = String.valueOf(s.get("status")); String ck2 = String.valueOf(s.get("check")); if ("未处理".equals(ck2)) { %>
     <tr>
       <td><%=s.get("id")%></td>
        <td><a href="<%=request.getContextPath()%>/animals?source=submission&id=<%=s.get("id")%>&from=readonly"><%=s.get("name")%></a></td>
       <td><%=s.get("species")%></td>
       <td><span class="<%= "未批准".equals(st2)?"status-bad":"批准".equals(st2)?"status-ok":"" %>"><%=st2%></span></td>
       <td><%=ck2%></td>
       <td><%=fmt.format((java.util.Date)s.get("created_at"))%></td>
     </tr>
   <% } } } %>
  <% if (subs != null) { %>
  <% for (Map<String,Object> s: subs) { String st2 = String.valueOf(s.get("status")); String ck2 = String.valueOf(s.get("check")); if ("已处理".equals(ck2)) { %>
     <tr>
       <td><%=s.get("id")%></td>
        <td><a href="<%=request.getContextPath()%>/animals?source=submission&id=<%=s.get("id")%>&from=readonly"><%=s.get("name")%></a></td>
       <td><%=s.get("species")%></td>
       <td><span class="<%= "未批准".equals(st2)?"status-bad":"批准".equals(st2)?"status-ok":"" %>"><%=st2%></span></td>
       <td><%=ck2%></td>
       <td><%=fmt.format((java.util.Date)s.get("created_at"))%></td>
     </tr>
   <% } } } %>
   <% if (subs == null || subs.isEmpty()) { %>
     <tr><td colspan="5">暂无添加申请</td></tr>
   <% } %>
 </table>
<hr/>
</div>
</body>
</html>
