const { createApp, ref, computed, reactive } = Vue;

const app = createApp({
  setup() {
    // 登录状态
    const isLoggedIn = ref(false);
    const currentUser = ref('管理员');
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
    const roomTypes = ['大床房', '标准间', '豪华套房', '单人间'];

    // Mock 数据 - 12个房间
    const rooms = ref([
      { id: 1, no: '1001', typeName: '标准间', capacity: 2, todayPrice: 199, status: 0, isHourly: false, checkInTime: '', checkOutTime: '', demandCount: 0 },
      { id: 2, no: '1002', typeName: '大床房', capacity: 2, todayPrice: 229, status: 1, isHourly: false, checkInTime: '14:00', checkOutTime: '12:00', demandCount: 2 },
      { id: 3, no: '1003', typeName: '单人间', capacity: 1, todayPrice: 149, status: 2, isHourly: false, checkInTime: '', checkOutTime: '', demandCount: 0 },
      { id: 4, no: '1004', typeName: '豪华套房', capacity: 4, todayPrice: 499, status: 0, isHourly: true, checkInTime: '', checkOutTime: '', demandCount: 0 },
      { id: 5, no: '1005', typeName: '标准间', capacity: 2, todayPrice: 199, status: 1, isHourly: false, checkInTime: '15:30', checkOutTime: '11:00', demandCount: 1 },
      { id: 6, no: '1006', typeName: '大床房', capacity: 2, todayPrice: 229, status: 0, isHourly: false, checkInTime: '', checkOutTime: '', demandCount: 0 },
      { id: 7, no: '2001', typeName: '单人间', capacity: 1, todayPrice: 149, status: 1, isHourly: true, checkInTime: '10:00', checkOutTime: '14:00', demandCount: 0 },
      { id: 8, no: '2002', typeName: '豪华套房', capacity: 4, todayPrice: 499, status: 2, isHourly: false, checkInTime: '', checkOutTime: '', demandCount: 3 },
      { id: 9, no: '2003', typeName: '标准间', capacity: 2, todayPrice: 199, status: 0, isHourly: false, checkInTime: '', checkOutTime: '', demandCount: 0 },
      { id: 10, no: '2004', typeName: '大床房', capacity: 2, todayPrice: 229, status: 1, isHourly: false, checkInTime: '16:00', checkOutTime: '10:00', demandCount: 0 },
      { id: 11, no: '2005', typeName: '单人间', capacity: 1, todayPrice: 149, status: 0, isHourly: true, checkInTime: '', checkOutTime: '', demandCount: 1 },
      { id: 12, no: '2006', typeName: '豪华套房', capacity: 4, todayPrice: 499, status: 1, isHourly: false, checkInTime: '12:00', checkOutTime: '14:00', demandCount: 0 }
    ]);

    // 抽屉
    const drawerVisible = ref(false);
    const drawerTitle = ref('');
    const currentRoom = ref({});

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
        // 1) 发起登录（不要用 status 200/302 判断成败）
        await fetch('/login', {
          method: 'POST',
          headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
          body: formData.toString(),
          credentials: 'same-origin',
          redirect: 'manual'
        });

        // 2) 探针：请求一个必须登录的接口（你的 SecurityConfig 已要求 /api/** authenticated）
        const probe = await fetch('/api/room-types', {
          credentials: 'same-origin',
          redirect: 'manual'
        });

        if (probe.status === 200) {
          isLoggedIn.value = true;
          currentUser.value = loginForm.username;
          ElementPlus.ElMessage.success('登录成功');
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
      loginForm.username = '';
      loginForm.password = '';
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
    const openDrawer = (room) => {
      currentRoom.value = room;
      drawerTitle.value = `房间 ${room.no} - 详情`;
      drawerVisible.value = true;
    };

    // 标记待清洁
    const markClean = () => {
      currentRoom.value.status = 2;
      const idx = rooms.value.findIndex(r => r.id === currentRoom.value.id);
      if (idx !== -1) {
        rooms.value[idx].status = 2;
      }
      ElementPlus.ElMessage.success('已标记为待清洁');
    };

    // 标记空房
    const markEmpty = () => {
      currentRoom.value.status = 0;
      const idx = rooms.value.findIndex(r => r.id === currentRoom.value.id);
      if (idx !== -1) {
        rooms.value[idx].status = 0;
      }
      ElementPlus.ElMessage.success('已标记为空房');
    };

    // 新增需求
    const addDemand = () => {
      const idx = rooms.value.findIndex(r => r.id === currentRoom.value.id);
      if (idx !== -1) {
        rooms.value[idx].demandCount++;
        currentRoom.value.demandCount = rooms.value[idx].demandCount;
      }
      ElementPlus.ElMessage.success('已添加新需求');
    };

    return {
      isLoggedIn,
      currentUser,
      loginForm,
      filters,
      roomTypes,
      filteredRooms,
      drawerVisible,
      drawerTitle,
      currentRoom,
      handleLogin,
      handleLogout,
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
