<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-table :columns="columns"
               :row-key="record => record.id"
               :pagination="pagination"
               :loading="loading"
               @change="handleTableChange"
               :data-source="ebooks"
      >
        <template #headerCell="{ column }">
        </template>

        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'cover'">
            <img v-if="cover" :src="cover" alt="avatar" />
          </template>
          <template v-else-if="column.key === 'action'">
            <a-space size="small">
              <a-button type="primary" @click="edit(record)">
                编辑
              </a-button>
              <a-button type="danger">
                删除
              </a-button>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal
      v-model:visible="modalVisible"
      title="电子书表单"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >

    <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="封面">
        <a-input v-model:value="ebook.cover" />
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebook.name" />
      </a-form-item>
      <a-form-item label="分类">
        <a-input v-model:value="ebook.category1Id" />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="ebook.description" type="textarea" />
      </a-form-item>
    </a-form>
  </a-modal>

</template>



<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import axios from 'axios';

export default defineComponent({
  name: 'AdminEbook',
  setup() {
    const ebooks = ref();
    const pagination = ref({
      current: 1,
      pageSize: 2,
      total: 0
    });
    const loading = ref(false);

    const columns = [
      {
        title: '封面',
        dataIndex: 'cover',
        key: 'cover'
      },
      {
        title: '名称',
        dataIndex: 'name',
        key: 'name'
      },
      {
        title: '分类1',
        key: 'category1Id',
        dataIndex: 'category1Id'
      },
      {
        title: '分类2',
        key: 'category2Id',
        dataIndex: 'category2Id'
      },
      {
        title: '文档数',
        dataIndex: 'docCount',
        key: 'docCount'
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount',
        key: 'viewCount'
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount',
        key: 'voteCount'
      },
      {
        title: 'Action',
        key: 'action'
      }
    ];

    /**
     * 数据查询
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      axios.get("/ebook/list", {
        params: {
          page: params.page,
          size: params.size
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        ebooks.value = data.content.list;

        pagination.value.current = params.page;
        pagination.value.total = data.content.total;
      });
    };

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥：" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    // -------- 表单 ---------
    /**
     * 数组，[100, 101]对应：前端开发 / Vue
     */
    const ebook = ref({});
    const modalText = ref<string>('Content of the modal');
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalText.value = 'The modal will be closed after two seconds';
      modalLoading.value = true;
      setTimeout(() => {
        modalVisible.value = false;
        modalLoading.value = false;
      }, 2000);
    };

    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      ebook.value = record;
    };

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });

    return {
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange,

      modalText,
      modalVisible,
      modalLoading,
      ebook,
      edit,
      handleModalOk
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