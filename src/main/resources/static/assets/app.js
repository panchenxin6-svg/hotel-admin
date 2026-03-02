const { createApp, ref, computed, reactive, watch } = Vue;

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

    // 筛选条件
    const filters = reactive({
      status: null,
      isHourly: null,
      roomType: null,
      date: new Date()
    });

    // 房型列表
    const roomTypes = ref([]);

    // 房间数据
    const rooms = ref([]);

    // 抽屉
    const drawerVisible = ref(false);
    const drawerTitle = ref('');
    const currentRoom = ref({});
    const reqList = ref([]);

    // 格式化日期
    const formatDate = (d) => {
      const date = new Date(d);
      const y = date.getFullYear();
      const m = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${y}-${m}-${day}`;
    };

    // 格式化时间戳为 HH:mm
    const formatTs = (dtStr) => {
      if (!dtStr) return '';
      // 简单截取 ISO 字符串的时间部分
      return dtStr.substring(11, 16);
    };

    // 加载看板数据
    const loadDashboard = async () => {
      const dateStr = formatDate(filters.date);
      try {
        const resp = await fetch(`/api/rooms/dashboard?date=${dateStr}`, {
          credentials: 'same-origin'
        });
        if (resp.ok) {
          const data = await resp.json();
          if (data.code === 200) {
            // 映射字段
            rooms.value = data.data.map(item => ({
              id: item.id,
              no: item.roomNo,
              typeName: item.roomTypeName,
              todayPrice: item.todayPrice,
              status: item.status,
              isHourly: item.isHourly === 1,
              demandCount: item.demandCount,
              capacity: 0,
              checkInTime: '',
              checkOutTime: ''
            }));
          }
        }
      } catch (e) {
        console.error('加载看板数据失败', e);
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

    // 加载营业额数据
    const loadRevenue = async () => {
      if (!window.echarts) return;
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
          if (data.code === 200) {
            const dates = data.data.map(it => it.date);
            const revenues = data.data.map(it => parseFloat(it.revenue) || 0);
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
        }
      } catch (e) {
        console.error('加载营业额失败', e);
      }
    };

    // 监听日期变化
    watch(() => filters.date, () => {
      if (isLoggedIn.value) {
        loadDashboard();
        if (isAdmin.value) {
          loadRevenue();
        }
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
        // 1) 发起登录
        await fetch('/login', {
          method: 'POST',
          headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
          body: formData.toString(),
          credentials: 'same-origin',
          redirect: 'manual'
        });

        // 2) 用 /api/me 验证登录并获取用户信息
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
      loginForm.username = '';
      loginForm.password = '';
      rooms.value = [];
    };

    // 用户管理（占位）
    const openUserAdmin = () => {
      ElementPlus.ElMessage.info("TODO: 用户管理");
    };

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
      // 加载需求列表
      await loadReqs(room.id);
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

    // 标记空房
    const markEmpty = async () => {
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
              // 刷新需求列表和看板
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
      loginForm,
      filters,
      roomTypes,
      filteredRooms,
      drawerVisible,
      drawerTitle,
      currentRoom,
      reqList,
      formatTs,
      handleLogin,
      handleLogout,
      openUserAdmin,
      getStatusText,
      getStatusType,
      openDrawer,
      markClean,
      markEmpty,
      addDemand
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
