<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<html>
<head><title>流浪动物管理</title>
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
  <div class="right">
    <a href="<%=request.getContextPath()%>/logout">退出</a>
  </div>
</div>
<div class="page">
<div style="display:flex;justify-content:space-between;align-items:center">
  <div>
    <button id="addBtn">添加新救助动物</button>
  </div>
  <button onclick="location.reload()">刷新</button>
  </div>
<div id="addPanel" style="display:none">
  <h3>添加新救助动物</h3>
  <form method="post" action="<%=request.getContextPath()%>/admin/animals" enctype="multipart/form-data">
    <input type="hidden" name="action" value="addAnimal"/>
    <div class="form-row"><label>名称</label><input name="name"/></div>
    <div class="form-row"><label>物种</label><input name="species"/></div>
    <div class="form-row"><label>品种</label><input name="breed"/></div>
    <div class="form-row"><label>年龄</label><input name="age"/></div>
    <div class="form-row"><label>健康状态</label><input name="health_status"/></div>
    <div class="form-row"><label>救助地点</label><input name="rescue_location"/></div>
    <div class="form-row"><label>救助时间</label><input name="rescue_time" data-date-picker="1" required placeholder="YYYY.MM.DD"/><span class="date-tip">点击选择日期</span></div>
    <div class="form-row"><label>照片URL</label><input name="photo_url" data-image-url="1" data-name="photo_file" required/></div>
    <div class="form-row"><label>描述</label><input name="description"/></div>
    <div class="form-actions"><button type="submit">添加</button></div>
  </form>
  <hr/>
</div>
<h3>现有动物</h3>
<table border="1" cellspacing="0" cellpadding="6">
  <tr><th>ID</th><th>名称</th><th>物种</th><th>品种</th><th>年龄</th><th>健康</th><th>已领养</th><th>操作</th></tr>
  <% java.util.List<java.util.Map<String,Object>> animals = (java.util.List<java.util.Map<String,Object>>)request.getAttribute("animalsList"); if (animals != null) for (java.util.Map<String,Object> r: animals) { %>
    <tr>
      <td><%=r.get("id")%></td>
      <td><a href="<%=request.getContextPath()%>/animals?from=admin&id=<%=r.get("id")%>"><%=r.get("name")%></a></td>
      <td><%=r.get("species")%></td>
      <td><%=r.get("breed")%></td>
      <td><%=r.get("age")%></td>
      <td><%=r.get("health_status")%></td>
      <td><%=((Integer)r.get("adopted")==1?"是":"否")%></td>
      <td>
        <form method="post" action="<%=request.getContextPath()%>/admin/animals" style="display:inline" class="row-update">
          <input type="hidden" name="action" value="updateAnimal"/>
          <input type="hidden" name="id" value="<%=r.get("id")%>"/>
          <label>健康：<input name="health_status" value="<%=r.get("health_status")%>"/></label>
          <label>已领养：<select name="adopted"><option value="0" <%=((Integer)r.get("adopted")==0?"selected":"")%>>否</option><option value="1" <%=((Integer)r.get("adopted")==1?"selected":"")%>>是</option></select></label>
          <button type="submit">更新</button>
        </form>
        <form method="post" action="<%=request.getContextPath()%>/admin/animals" style="display:inline">
          <input type="hidden" name="action" value="deleteAnimal"/>
          <input type="hidden" name="id" value="<%=r.get("id")%>"/>
          <button type="submit">删除</button>
        </form>
      </td>
    </tr>
  <% } %>
  </table>
  <% String err = (String)request.getAttribute("error"); if (err == null) err = request.getParameter("error"); if (err != null) { %>
    <div style="color:#ff4d88;margin-top:8px"><%=err%></div>
  <% } %>

<!-- 审核内容已移动至 /admin/review 页面 -->

<script>
  const qp = new URLSearchParams(location.search);
  if(qp.get('ok') === '1') alert('更新成功');
  if(qp.get('error')) alert(qp.get('error'));
  document.querySelectorAll('form.row-update').forEach(f=>{
    f.addEventListener('submit', function(e){
      const health = this.querySelector('input[name=health_status]');
      if(health && !health.value.trim()){
        alert('请填写健康状态');
        e.preventDefault();
      }
    });
  });
  const addBtn = document.getElementById('addBtn');
  if(addBtn){ addBtn.addEventListener('click', ()=>{ const p = document.getElementById('addPanel'); if(p) p.style.display='block'; }); }
</script>
</div>
</body>
</html>
<script src="<%=request.getContextPath()%>/static/image-picker.js"></script>
<script src="<%=request.getContextPath()%>/static/date-picker.js"></script>
