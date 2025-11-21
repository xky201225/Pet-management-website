<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<html>
<head><title>审核</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/style.css">
<script src="<%=request.getContextPath()%>/static/bubbles.js"></script>
</head>
<body>
<div class="navbar">
  <div class="links">
    <a href="<%=request.getContextPath()%>/admin">管理员主页</a>
    <a href="<%=request.getContextPath()%>/admin/users">用户管理</a>
    <a href="<%=request.getContextPath()%>/admin/animals">动物管理</a>
    <a href="<%=request.getContextPath()%>/admin/adoptions">领养管理</a>
    <a href="<%=request.getContextPath()%>/admin/forum">论坛管理</a>
    <a href="<%=request.getContextPath()%>/admin/knowledge">护养知识管理</a>
    <a href="<%=request.getContextPath()%>/admin/review">待处理审核</a>
  </div>
  <div class="right"><a href="<%=request.getContextPath()%>/logout">退出</a></div>
</div>
<div class="page">
  <div style="display:flex;gap:8px;margin-bottom:12px">
    <button onclick="showTab('subs')">待处理的用户提交流浪动物</button>
    <button onclick="showTab('apps')">待处理的领养申请</button>
  </div>
  <div id="tab-subs">
    <h3>用户提交流浪动物待审核</h3>
    <table border="1" cellspacing="0" cellpadding="6">
      <tr><th>ID</th><th>名称</th><th>物种</th><th>提交用户</th><th>操作</th></tr>
      <% List<Map<String,Object>> subs = (List<Map<String,Object>>)request.getAttribute("subsList"); if (subs != null) for (Map<String,Object> s: subs) { %>
        <tr>
          <td><%=s.get("id")%></td>
          <td><a href="<%=request.getContextPath()%>/animals?from=admin&source=submission&id=<%=s.get("id")%>"><%=s.get("name")%></a></td>
          <td><%=s.get("species")%></td>
          <td><%=s.get("username")%></td>
          <td>
            <form method="post" action="<%=request.getContextPath()%>/admin/animals" style="display:inline">
              <input type="hidden" name="action" value="approveSubmission"/>
              <input type="hidden" name="id" value="<%=s.get("id")%>"/>
              <button type="submit">通过</button>
            </form>
            <form method="post" action="<%=request.getContextPath()%>/admin/animals" style="display:inline">
              <input type="hidden" name="action" value="rejectSubmission"/>
              <input type="hidden" name="id" value="<%=s.get("id")%>"/>
              <button type="submit">拒绝</button>
            </form>
          </td>
        </tr>
      <% } %>
    </table>
  </div>
  <div id="tab-apps" style="display:none">
    <h3>待处理的领养申请</h3>
    <table border="1" cellspacing="0" cellpadding="6">
      <tr><th>ID</th><th>用户</th><th>动物</th><th>居住环境</th><th>饲养经验</th><th>备注</th><th>提交时间</th><th>操作</th></tr>
      <% List<Map<String,Object>> apps = (List<Map<String,Object>>)request.getAttribute("pendingApps"); if (apps != null) for (Map<String,Object> a: apps) { %>
        <tr>
          <td><%=a.get("id")%></td>
          <td><%=a.get("username")%></td>
          <td><a href="<%=request.getContextPath()%>/animals?from=admin&id=<%=a.get("animal_id")%>"><%=a.get("animal_name")%></a></td>
          <td style="white-space:pre-wrap"><%=a.get("residence")%></td>
          <td style="white-space:pre-wrap"><%=a.get("experience")%></td>
          <td style="white-space:pre-wrap"><%=a.get("message")%></td>
          <td><%=a.get("created_at")%></td>
          <td>
            <form method="post" action="<%=request.getContextPath()%>/admin/adoptions" style="display:inline">
              <input type="hidden" name="action" value="approveApp"/>
              <input type="hidden" name="id" value="<%=a.get("id")%>"/>
              <input name="opinion" placeholder="通过提示"/>
              <button type="submit">通过</button>
            </form>
            <form method="post" action="<%=request.getContextPath()%>/admin/adoptions" style="display:inline">
              <input type="hidden" name="action" value="rejectApp"/>
              <input type="hidden" name="id" value="<%=a.get("id")%>"/>
              <input name="opinion" placeholder="审核意见"/>
              <button type="submit">拒绝</button>
            </form>
          </td>
        </tr>
      <% } %>
    </table>
  </div>
</div>
<script>
  function showTab(which){
    document.getElementById('tab-subs').style.display = which==='subs'?'block':'none';
    document.getElementById('tab-apps').style.display = which==='apps'?'block':'none';
  }
</script>
</body>
</html>