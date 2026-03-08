const { createApp, ref, computed, reactive, watch, nextTick } = Vue;

const app = createApp({
  setup() {
    // 登录状态
    const isLoggedIn = ref(false);
    const currentUser = ref('管理员');
    const currentRole = ref('');
    const loginForm = reactive({
      username: '',
      password: ''
    });

    // 页面切换
    const activePage = ref('home');

    // KPI 数据
    const kpi = reactive({
      todayRevenue: '0',
      todayOccupied: 0,
      toClean: 0
    });

    // 筛选条件
    const filters = reactive({
      status: null,
      isHourly: null,
      roomType: null,
      date: new Date(),
      q: ''
    });

    // 确保日期有效
    if (!filters.date || isNaN(new Date(filters.date).getTime())) {
      filters.date = new Date();
    }

    // 房型列表
    const roomTypes = ref([]);

    // 房间数据
    const rooms = ref([]);

    // 抽屉
    const drawerVisible = ref(false);
    const drawerTitle = ref('');
    const currentRoom = ref({});
    const reqList = ref([]);
    
    // 住客列表
    const guestList = ref([]);

    // 入住弹窗
    const checkInVisible = ref(false);
    const checkInForm = reactive({
      guestCount: 1,
      mainGuestName: '',
      mainGuestGender: 1,
      mainGuestIdCard: '',
      otherGuests: [],
      remark: '',
      expectedCheckOutAt: null
    });

    // 统计数据
    const statsData = reactive({
      channelStats: [],
      priceTrend: [],
      revenueData: []
    });
    const statsLoading = ref(false);

    // 用户管理数据
    const users = ref([]);
    const usersLoading = ref(false);
    const userFormVisible = ref(false);
    const userForm = reactive({
      username: '',
      password: '',
      role: 'USER'
    });
    const editingUserId = ref(null);

    // 工单相关数据
    const taskLoading = ref(false);
    const taskList = ref([]);
    const taskFilter = reactive({ status: null });
    const taskFormVisible = ref(false);
    const taskForm = reactive({
      roomId: null,
      title: '',
      content: '',
      remark: ''
    });
    const taskAssignVisible = ref(false);
    const taskAssignForm = reactive({
      id: null,
      assignedTo: ''
    });

    // 格式化日期
    const formatDate = (d) => {
      if (!d) {
        d = new Date();
      }
      const date = new Date(d);
      if (isNaN(date.getTime())) {
        d = new Date();
        date = new Date(d);
      }
      const y = date.getFullYear();
      const m = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${y}-${m}-${day}`;
    };

    // 格式化日期时间
    const fmt = (dt) => {
      if (!dt) return '';
      const d = new Date(dt);
      const y = d.getFullYear();
      const m = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      const h = String(d.getHours()).padStart(2, '0');
      const min = String(d.getMinutes()).padStart(2, '0');
      return `${y}-${m}-${day} ${h}:${min}`;
    };

    // 是否临近退房（30分钟内）
    const isUrgent = (expectedCheckOutAt) => {
      if (!expectedCheckOutAt) return false;
      const now = new Date();
      const checkOut = new Date(expectedCheckOutAt);
      const diffMs = checkOut - now;
      const diffMin = diffMs / (1000 * 60);
      return diffMin > 0 && diffMin <= 30;
    };

    // 格式化时间戳为 HH:mm
    const formatTs = (dtStr) => {
      if (!dtStr) return '';
      return dtStr.substring(11, 16);
    };
    
    // 身份证号脱敏
    const maskIdCard = (idCard) => {
      if (!idCard || idCard.length < 8) return idCard;
      return idCard.substring(0, 4) + '********' + idCard.substring(idCard.length - 4);
    };
    
    // 入住人数变化时调整表单
    const onGuestCountChange = (count) => {
      const currentLen = checkInForm.otherGuests.length;
      if (count > 1) {
        // 添加或删除其他入住人
        if (count - 1 > currentLen) {
          for (let i = currentLen; i < count - 1; i++) {
            checkInForm.otherGuests.push({ name: '', gender: 1, idCard: '' });
          }
        } else if (count - 1 < currentLen) {
          checkInForm.otherGuests.splice(count - 1);
        }
      }
    };

    // 计算 KPI
    const updateKpi = () => {
      // 今日入住 = status === 1 的房间数
      kpi.todayOccupied = rooms.value.filter(r => r.status === 1).length;
      // 待清洁 = status === 2 的房间数
      kpi.toClean = rooms.value.filter(r => r.status === 2).length;
    };

    // 加载看板数据
    const loadDashboard = async () => {
      const dateStr = formatDate(filters.date);
      console.log('加载看板，日期:', dateStr);
      let url = `/api/rooms/dashboard?date=${dateStr}`;
      if (filters.q) {
        url += `&q=${encodeURIComponent(filters.q)}`;
      }
      console.log('请求URL:', url);
      try {
        const resp = await fetch(url, {
          credentials: 'same-origin'
        });
        console.log('响应状态:', resp.status);
        
        // 获取响应文本
        const text = await resp.text();
        console.log('响应内容:', text);
        
        if (!resp.ok) {
          ElementPlus.ElMessage.error('HTTP错误 ' + resp.status + ': ' + text);
          return;
        }
        
        const data = JSON.parse(text);
        console.log('解析后的数据:', data);
        
        if (data.code === 200) {
          rooms.value = data.data.map(item => ({
            id: item.id,
            no: item.roomNo,
            typeName: item.roomTypeName,
            todayPrice: item.todayPrice,
            status: item.status,
            isHourly: item.isHourly === 1,
            demandCount: item.demandCount,
            capacity: 0,
            // Stay info
            guestName: item.guestName || '',
            guestIdCard: item.guestIdCard || '',
            checkInAt: item.checkInAt || '',
            expectedCheckOutAt: item.expectedCheckOutAt || '',
            channelName: item.channelName || ''
          }));
          console.log('房间数据加载成功，数量:', rooms.value.length);
          // 更新 KPI
          updateKpi();
        } else {
          console.error('API返回错误:', data.message);
          ElementPlus.ElMessage.error('加载数据失败: ' + (data.message || ''));
        }
      } catch (e) {
        console.error('加载看板数据异常:', e);
        ElementPlus.ElMessage.error('加载数据失败: ' + e.message);
      }
    };

    // 加载房型列表
    const loadRoomTypes = async () => {
      try {
        const resp = await fetch('/api/room-types', { credentials: 'same-origin' });
        if (resp.ok) {
          const data = await resp.json();
          if (data.code === 200) {
            roomTypes.value = data.data.map(rt => rt.typeName);
          }
        }
      } catch (e) {
        console.error('加载房型失败', e);
      }
    };

    // 加载需求列表
    const loadReqs = async (roomId) => {
      const dateStr = formatDate(filters.date);
      try {
        const resp = await fetch(`/api/rooms/${roomId}/req?date=${dateStr}`, {
          credentials: 'same-origin'
        });
        if (resp.ok) {
          const data = await resp.json();
          if (data.code === 200) {
            reqList.value = data.data || [];
          }
        }
      } catch (e) {
        console.error('加载需求列表失败', e);
      }
    };

    // ECharts 图表实例
    let revChartInstance = null;
    let statsRevChartInstance = null;

    // 加载营业额数据
    const loadRevenue = async () => {
      if (!window.echarts) return;
      await nextTick();
      const today = new Date();
      const weekAgo = new Date(today);
      weekAgo.setDate(weekAgo.getDate() - 6);
      const fromStr = formatDate(weekAgo);
      const toStr = formatDate(today);
      try {
        const resp = await fetch(`/api/admin/stats/revenue?from=${fromStr}&to=${toStr}`, {
          credentials: 'same-origin'
        });
        if (resp.ok) {
          const data = await resp.json();
          if (data.code === 200 && data.data) {
            const dates = data.data.map(it => it.date);
            const revenues = data.data.map(it => parseFloat(it.revenue) || 0);
            // 更新 KPI 营业额（取最后一天）
            if (revenues.length > 0) {
              kpi.todayRevenue = revenues[revenues.length - 1];
            }
            if (!revChartInstance) {
              revChartInstance = echarts.init(document.getElementById('revChart'));
            }
            revChartInstance.setOption({
              tooltip: { trigger: 'axis' },
              xAxis: { type: 'category', data: dates },
              yAxis: { type: 'value', name: '金额(元)' },
              series: [{ type: 'bar', data: revenues, itemStyle: { color: '#409eff' } }]
            });
          }
        } else {
          console.error('加载营业额失败 HTTP:', resp.status);
        }
      } catch (e) {
        console.error('加载营业额失败', e);
      }
    };

    // 监听日期变化
    watch(() => filters.date, () => {
      if (isLoggedIn.value) {
        loadDashboard();
        if (activePage.value === 'home' && isAdmin.value) {
          loadRevenue();
        }
      }
    });

    // 监听页面切换
    watch(activePage, (newVal) => {
      if (newVal === 'home' && isAdmin.value) {
        loadRevenue();
      } else if (newVal === 'rooms') {
        loadDashboard();
      } else if (newVal === 'stats' && isAdmin.value) {
        loadStats();
      } else if (newVal === 'users' && isAdmin.value) {
        loadUsers();
      }
    });

    // 筛选后的房间
    const filteredRooms = computed(() => {
      return rooms.value.filter(room => {
        if (filters.status !== null && filters.status !== '' && room.status !== filters.status) {
          return false;
        }
        if (filters.isHourly !== null && filters.isHourly !== '' && room.isHourly !== filters.isHourly) {
          return false;
        }
        if (filters.roomType && room.typeName !== filters.roomType) {
          return false;
        }
        return true;
      });
    });

    // 是否为管理员
    const isAdmin = computed(() => {
      return currentRole.value === 'ADMIN' || currentRole.value === 'SUPER_ADMIN';
    });

    // 登录
    const handleLogin = async () => {
      if (!loginForm.username || !loginForm.password) {
        ElementPlus.ElMessage.warning('请输入用户名和密码');
        return;
      }

      const formData = new URLSearchParams();
      formData.append('username', loginForm.username);
      formData.append('password', loginForm.password);

      try {
        await fetch('/login', {
          method: 'POST',
          headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
          body: formData.toString(),
          credentials: 'same-origin',
          redirect: 'manual'
        });

        const resp = await fetch('/api/me', {
          credentials: 'same-origin',
          redirect: 'manual'
        });

        if (resp.status === 200) {
          const data = await resp.json();
          if (data.code === 200) {
            isLoggedIn.value = true;
            currentUser.value = data.data.username;
            currentRole.value = data.data.role;
            ElementPlus.ElMessage.success('登录成功');
            // 加载数据
            await loadRoomTypes();
            await loadDashboard();
            // 管理员加载营业额图表
            if (isAdmin.value) {
              await loadRevenue();
            }
          } else {
            ElementPlus.ElMessage.error('用户名或密码错误');
          }
        } else {
          ElementPlus.ElMessage.error('用户名或密码错误');
        }
      } catch (e) {
        ElementPlus.ElMessage.error('用户名或密码错误');
      }
    };

    // 退出
    const handleLogout = async () => {
      try {
        await fetch('/logout', { method: 'POST', credentials: 'same-origin' });
      } catch (e) {}
      isLoggedIn.value = false;
      currentRole.value = '';
      activePage.value = 'home';
      loginForm.username = '';
      loginForm.password = '';
      rooms.value = [];
    };

    // 用户管理（占位）
    const openUserAdmin = () => {
      activePage.value = 'users';
      loadUsers();
    };

    // 加载工单列表
    const loadTasks = async () => {
      taskLoading.value = true;
      try {
        let url = '/api/tasks';
        const params = [];
        if (taskFilter.status !== null && taskFilter.status !== '') {
          params.push(`status=${taskFilter.status}`);
        }
        if (params.length > 0) {
          url += '?' + params.join('&');
        }
        const resp = await fetch(url, { credentials: 'same-origin' });
        if (resp.ok) {
          const data = await resp.json();
          if (data.code === 200) {
            taskList.value = data.data || [];
          }
        }
      } catch (e) {
        console.error('加载工单失败', e);
      } finally {
        taskLoading.value = false;
      }
    };

    // 打开新建工单弹窗
    const openAddTask = () => {
      taskForm.roomId = null;
      taskForm.title = '';
      taskForm.content = '';
      taskForm.remark = '';
      taskFormVisible.value = true;
    };

    // 创建工单
    const handleAddTask = async () => {
      if (!taskForm.title || !taskForm.content) {
        ElementPlus.ElMessage.warning('请填写标题和需求内容');
        return;
      }
      try {
        const resp = await fetch('/api/tasks', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(taskForm),
          credentials: 'same-origin'
        });
        if (resp.ok) {
          const data = await resp.json();
          if (data.code === 200) {
            taskFormVisible.value = false;
            await loadTasks();
            ElementPlus.ElMessage.success('工单创建成功');
          } else {
            ElementPlus.ElMessage.error(data.message || '创建失败');
          }
        }
      } catch (e) {
        ElementPlus.ElMessage.error('创建失败');
      }
    };

    // 打开指派弹窗
    const openAssignTask = (task) => {
      taskAssignForm.id = task.id;
      taskAssignForm.assignedTo = '';
      taskAssignVisible.value = true;
    };

    // 指派工单
    const handleAssignTask = async () => {
      if (!taskAssignForm.assignedTo) {
        ElementPlus.ElMessage.warning('请选择执行人');
        return;
      }
      try {
        const resp = await fetch(`/api/tasks/${taskAssignForm.id}/assign`, {
          method: 'PATCH',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ assignedTo: taskAssignForm.assignedTo }),
          credentials: 'same-origin'
        });
        if (resp.ok) {
          const data = await resp.json();
          if (data.code === 200) {
            taskAssignVisible.value = false;
            await loadTasks();
            ElementPlus.ElMessage.success('指派成功');
          } else {
            ElementPlus.ElMessage.error(data.message || '指派失败');
          }
        }
      } catch (e) {
        ElementPlus.ElMessage.error('指派失败');
      }
    };

    // 完成任务
    const finishTask = async (task) => {
      try {
        const resp = await fetch(`/api/tasks/${task.id}/done`, {
          method: 'PATCH',
          credentials: 'same-origin'
        });
        if (resp.ok) {
          const data = await resp.json();
          if (data.code === 200) {
            await loadTasks();
            ElementPlus.ElMessage.success('已完成');
          } else {
            ElementPlus.ElMessage.error(data.message || '操作失败');
          }
        }
      } catch (e) {
        ElementPlus.ElMessage.error('操作失败');
      }
    };

    // 获取工单状态文本
    const getTaskStatusText = (status) => {
      const map = { 0: '待处理', 1: '进行中', 2: '已完成' };
      return map[status] || '未知';
    };

    // 获取工单状态标签类型
    const getTaskStatusType = (status) => {
      const map = { 0: 'info', 1: 'warning', 2: 'success' };
      return map[status] || 'info';
    };

    // 监听页面切换
    watch(activePage, (newVal) => {
      if (newVal === 'tasks') {
        loadTasks();
      }
    });

    // 获取状态文本
    const getStatusText = (status) => {
      const map = { 0: '空房', 1: '已入住', 2: '待清洁' };
      return map[status] || '未知';
    };

    // 获取状态标签类型
    const getStatusType = (status) => {
      const map = { 0: 'success', 1: 'danger', 2: 'warning' };
      return map[status] || 'info';
    };

    // 打开抽屉
    const openDrawer = async (room) => {
      currentRoom.value = room;
      drawerTitle.value = `房间 ${room.no} - 详情`;
      drawerVisible.value = true;
      await loadReqs(room.id);
      await loadGuests(room.id);
    };
    
    // 加载住客列表
    const loadGuests = async (roomId) => {
      try {
        const resp = await fetch(`/api/rooms/${roomId}/guests`, {
          credentials: 'same-origin'
        });
        if (resp.ok) {
          const data = await resp.json();
          if (data.code === 200) {
            guestList.value = data.data || [];
          }
        }
      } catch (e) {
        console.error('加载住客失败', e);
        guestList.value = [];
      }
    };

    // 标记待清洁
    const markClean = async () => {
      try {
        const resp = await fetch(`/api/rooms/${currentRoom.value.id}/status`, {
          method: 'PATCH',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ status: 2 }),
          credentials: 'same-origin'
        });
        if (resp.ok) {
          const data = await resp.json();
          if (data.code === 200) {
            currentRoom.value.status = 2;
            await loadDashboard();
            ElementPlus.ElMessage.success('已标记为待清洁');
          } else {
            ElementPlus.ElMessage.error('操作失败');
          }
        } else {
          ElementPlus.ElMessage.error('操作失败');
        }
      } catch (e) {
        ElementPlus.ElMessage.error('操作失败');
      }
    };

    // 标记空房（仅限待清洁状态）
    const markEmpty = async () => {
      if (currentRoom.value.status !== 2) {
        ElementPlus.ElMessage.warning('只有待清洁的房间才能标记为空房');
        return;
      }
      try {
        await ElementPlus.ElMessageBox.confirm('确定要将该房间标记为空房吗？', '确认', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });

        const resp = await fetch(`/api/rooms/${currentRoom.value.id}/status`, {
          method: 'PATCH',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ status: 0 }),
          credentials: 'same-origin'
        });
        if (resp.ok) {
          const data = await resp.json();
          if (data.code === 200) {
            currentRoom.value.status = 0;
            await loadDashboard();
            ElementPlus.ElMessage.success('已标记为空房');
          } else {
            ElementPlus.ElMessage.error('操作失败');
          }
        } else {
          ElementPlus.ElMessage.error('操作失败');
        }
      } catch (e) {
        // 用户取消不报错
      }
    };

    // 打开入住弹窗
    const openCheckIn = () => {
      checkInForm.guestCount = 1;
      checkInForm.mainGuestName = '';
      checkInForm.mainGuestGender = 1;
      checkInForm.mainGuestIdCard = '';
      checkInForm.otherGuests = [];
      checkInForm.remark = '';
      checkInForm.expectedCheckOutAt = null;
      checkInVisible.value = true;
    };

    // 办理入住
    const handleCheckIn = async () => {
      if (!checkInForm.mainGuestName || !checkInForm.mainGuestIdCard) {
        ElementPlus.ElMessage.warning('请填写主客姓名和身份证');
        return;
      }
      // 验证其他入住人
      if (checkInForm.guestCount > 1) {
        for (let i = 0; i < checkInForm.otherGuests.length; i++) {
          const g = checkInForm.otherGuests[i];
          if (!g.name || !g.idCard) {
            ElementPlus.ElMessage.warning('请填写第' + (i + 2) + '位入住人的信息');
            return;
          }
        }
      }
      try {
        const body = {
          guestCount: checkInForm.guestCount,
          mainGuestName: checkInForm.mainGuestName,
          mainGuestGender: checkInForm.mainGuestGender,
          mainGuestIdCard: checkInForm.mainGuestIdCard,
          otherGuests: checkInForm.otherGuests,
          remark: checkInForm.remark
        };
        if (checkInForm.expectedCheckOutAt) {
          body.expectedCheckOutAt = checkInForm.expectedCheckOutAt;
        }
        const resp = await fetch(`/api/rooms/${currentRoom.value.id}/checkin`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(body),
          credentials: 'same-origin'
        });
        if (resp.ok) {
          const data = await resp.json();
          if (data.code === 200) {
            checkInVisible.value = false;
            await loadDashboard();
            ElementPlus.ElMessage.success('入住成功');
          } else {
            ElementPlus.ElMessage.error(data.message || '入住失败');
          }
        } else {
          ElementPlus.ElMessage.error('入住失败');
        }
      } catch (e) {
        ElementPlus.ElMessage.error('入住失败');
      }
    };

    // 退房
    const handleCheckOut = async () => {
      try {
        await ElementPlus.ElMessageBox.confirm('确定要退房吗？退房后房间将变为待清洁状态', '确认退房', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });

        const resp = await fetch(`/api/rooms/${currentRoom.value.id}/checkout`, {
          method: 'POST',
          credentials: 'same-origin'
        });
        if (resp.ok) {
          const data = await resp.json();
          if (data.code === 200) {
            drawerVisible.value = false;
            await loadDashboard();
            ElementPlus.ElMessage.success('退房成功');
          } else {
            ElementPlus.ElMessage.error(data.message || '退房失败');
          }
        } else {
          ElementPlus.ElMessage.error('退房失败');
        }
      } catch (e) {
        // 用户取消不报错
      }
    };

    // 加载统计数据
    const loadStats = async () => {
      statsLoading.value = true;
      try {
        const today = new Date();
        const weekAgo = new Date(today);
        weekAgo.setDate(weekAgo.getDate() - 6);
        const fromStr = formatDate(weekAgo);
        const toStr = formatDate(today);

        // 加载营业额趋势
        const resp = await fetch(`/api/admin/stats/revenue?from=${fromStr}&to=${toStr}`, {
          credentials: 'same-origin'
        });
        if (resp.ok) {
          const data = await resp.json();
          if (data.code === 200 && data.data) {
            statsData.revenueData = data.data || [];
            
            // 渲染统计页图表
            await nextTick();
            if (window.echarts && document.getElementById('statsRevChart')) {
              if (!statsRevChartInstance) {
                statsRevChartInstance = echarts.init(document.getElementById('statsRevChart'));
              }
              const dates = statsData.revenueData.map(it => it.date);
              const revenues = statsData.revenueData.map(it => parseFloat(it.revenue) || 0);
              statsRevChartInstance.setOption({
                tooltip: { trigger: 'axis' },
                xAxis: { type: 'category', data: dates },
                yAxis: { type: 'value', name: '金额(元)' },
                series: [{ type: 'bar', data: revenues, itemStyle: { color: '#409eff' } }]
              });
            }
          }
        } else {
          console.error('加载统计数据失败 HTTP:', resp.status);
        }
      } catch (e) {
        console.error('加载统计数据失败', e);
      } finally {
        statsLoading.value = false;
      }
    };

    // 加载用户列表
    const loadUsers = async () => {
      usersLoading.value = true;
      try {
        const resp = await fetch('/api/admin/users', {
          credentials: 'same-origin'
        });
        if (resp.ok) {
          const data = await resp.json();
          if (data.code === 200) {
            users.value = data.data || [];
          }
        }
      } catch (e) {
        console.error('加载用户列表失败', e);
      } finally {
        usersLoading.value = false;
      }
    };

    // 创建/更新用户
    const saveUser = async () => {
      if (!userForm.username || !userForm.password) {
        ElementPlus.ElMessage.warning('请填写用户名和密码');
        return;
      }
      try {
        const resp = await fetch('/api/admin/users', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(userForm),
          credentials: 'same-origin'
        });
        if (resp.ok) {
          const data = await resp.json();
          if (data.code === 200) {
            userFormVisible.value = false;
            await loadUsers();
            ElementPlus.ElMessage.success('创建成功');
          } else {
            ElementPlus.ElMessage.error(data.message || '创建失败');
          }
        } else {
          ElementPlus.ElMessage.error('创建失败');
        }
      } catch (e) {
        ElementPlus.ElMessage.error('创建失败');
      }
    };

    // 打开新增用户弹窗
    const openAddUser = () => {
      userForm.username = '';
      userForm.password = '';
      userForm.role = 'USER';
      editingUserId.value = null;
      userFormVisible.value = true;
    };

    // 监听页面切换
    watch(activePage, (newVal) => {
      if (newVal === 'home' && isAdmin.value) {
        loadRevenue();
      } else if (newVal === 'rooms') {
        loadDashboard();
      } else if (newVal === 'stats' && isAdmin.value) {
        loadStats();
      } else if (newVal === 'users' && isAdmin.value) {
        loadUsers();
      }
    });

    // 新增需求
    const addDemand = async () => {
      try {
        const { value: content } = await ElementPlus.ElMessageBox.prompt('请输入需求内容', '新增需求', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.+/,
          inputErrorMessage: '内容不能为空'
        });

        if (content) {
          const dateStr = formatDate(filters.date);
          const resp = await fetch(`/api/rooms/${currentRoom.value.id}/req`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ content, date: dateStr }),
            credentials: 'same-origin'
          });
          if (resp.ok) {
            const data = await resp.json();
            if (data.code === 200) {
              await loadReqs(currentRoom.value.id);
              await loadDashboard();
              ElementPlus.ElMessage.success('已添加新需求');
            } else {
              ElementPlus.ElMessage.error('添加失败');
            }
          } else {
            ElementPlus.ElMessage.error('添加失败');
          }
        }
      } catch (e) {
        // 用户取消不报错
      }
    };

    return {
      isLoggedIn,
      currentUser,
      currentRole,
      isAdmin,
      activePage,
      kpi,
      loginForm,
      filters,
      roomTypes,
      filteredRooms,
      drawerVisible,
      drawerTitle,
      currentRoom,
      reqList,
      guestList,
      checkInVisible,
      checkInForm,
      statsData,
      statsLoading,
      users,
      usersLoading,
      userFormVisible,
      userForm,
      // 工单相关
      taskLoading,
      taskList,
      taskFilter,
      taskFormVisible,
      taskForm,
      taskAssignVisible,
      taskAssignForm,
      formatTs,
      fmt,
      maskIdCard,
      onGuestCountChange,
      isUrgent,
      getTaskStatusText,
      getTaskStatusType,
      handleLogin,
      handleLogout,
      openUserAdmin,
      getStatusText,
      getStatusType,
      openDrawer,
      loadDashboard,
      markClean,
      markEmpty,
      addDemand,
      openCheckIn,
      handleCheckIn,
      handleCheckOut,
      loadStats,
      loadUsers,
      saveUser,
      openAddUser,
      // 工单函数
      loadTasks,
      openAddTask,
      handleAddTask,
      openAssignTask,
      handleAssignTask,
      finishTask
    };
  }
});

// 注册 Element Plus 图标（安全写法）
const icons = window.ElementPlusIconsVue || {};
for (const [key, component] of Object.entries(icons)) {
  app.component(key, component);
}

app.use(ElementPlus);
app.mount('#app');
