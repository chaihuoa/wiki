<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form
            layout="inline"
            :model="formState"
        >
          <a-form-item>
            <a-input v-model:value="formState.name" placeholder="name">
              <template #prefix><UserOutlined style="color: rgba(0, 0, 0, 0.25)" /></template>
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize, loginName: formState.name})">
              查询
            </a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="save">
              新增
            </a-button>
          </a-form-item>
        </a-form>
      </p>

      <a-table :columns="columns"
               :row-key="record => record.id"
               :pagination="pagination"
               :loading="loading"
               @change="handleTableChange"
               :data-source="users"
      >
        <template #headerCell="{ column }">
        </template>

        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <a-space size="small">
              <a-button type="primary" @click="edit(record)">
                编辑
              </a-button>

              <a-popconfirm
                  title="Are you sure delete this user?"
                  ok-text="Yes"
                  cancel-text="No"
                  @confirm="handleDelete(record.id)"
              >
                <a-button type="danger">
                  删除
                </a-button>
              </a-popconfirm>

            </a-space>
          </template>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal
      v-model:visible="modalVisible"
      title="用户表单"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >

    <a-form :model="user" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="登录名">
        <a-input v-model:value="user.loginName" />
      </a-form-item>
      <a-form-item label="昵称">
        <a-input v-model:value="user.name" />
      </a-form-item>
      <a-form-item label="密码">
        <a-input v-model:value="user.password" type="textarea" />
      </a-form-item>
    </a-form>
  </a-modal>

</template>



<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";

export default defineComponent({
  name: 'AdminUser',
  setup() {
    const formState = ref();
    formState.value = {};
    const users = ref();
    const pagination = ref({
      current: 1,
      pageSize: 4,
      total: 0
    });
    const loading = ref(false);

    const columns = [
      {
        title: '登录名',
        dataIndex: 'loginName',
        key: 'loginName'
      },
      {
        title: '昵称',
        dataIndex: 'name',
        key: 'name'
      },
      {
        title: '密码',
        key: 'password',
        dataIndex: 'password'
      },
      {
        title: 'Action',
        key: 'action'
      }
    ];

    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/user/list", {
        params: {
          page: params.page,
          size: params.size,
          loginName: formState.value.name
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          users.value = data.content.list;

          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message);
        }
      });
    };

    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥：" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    // -------- Form ---------

    const user = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);

    const handleModalOk = () => {
      modalLoading.value = true;
      axios.post("/user/save", user.value).then((response) => {
        const data = response.data;
        modalLoading.value = false;

        if (data.success) {
          modalVisible.value = false;

          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        } else {
          message.error(data.message);
        }
      });
    };

    const edit = (record: any) => {
      modalVisible.value = true;
      user.value = Tool.copy(record);
    };

    const save = () => {
      modalVisible.value = true;
      user.value = {};
    };

    const handleDelete = (id: string) => {
      axios.delete("/user/delete/" + id).then((response) => {
        const data = response.data;

        if (data.success) {
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        } else {
          message.error(data.message);
        }
      });
    };

    onMounted(() => {
      handleQuery({
        page: pagination.value.current,
        size: pagination.value.pageSize
      });
    });

    return {
      users,
      pagination,
      columns,
      loading,
      handleTableChange,

      edit,
      save,
      handleDelete,
      handleQuery,

      modalVisible,
      modalLoading,
      user,
      handleModalOk,

      formState,
    }
  }
});
</script>

<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>