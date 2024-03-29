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
            <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">
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
               :data-source="ebooks"
      >
        <template #headerCell="{ column }">
        </template>

        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'cover'">
            <img v-if="column.key === 'cover'" :src="record.cover" alt="avatar" />
          </template>
          <template v-if="column.key === 'category'">
            <span>{{ getCategoryName(record.category1Id) }} / {{ getCategoryName(record.category2Id) }}</span>
          </template>
          <template v-else-if="column.key === 'action'">
            <a-space size="small">
              <router-link :to="'/admin/doc?ebookId=' + record.id">
                <a-button type="primary" @click="edit(record)">
                  文档管理
                </a-button>
              </router-link>

              <a-button type="primary" @click="edit(record)">
                编辑
              </a-button>

              <a-popconfirm
                  title="Are you sure delete this ebook?"
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
        <a-cascader
            v-model:value="categoryIds"
            :field-names="{ label: 'name', value: 'id', children: 'children' }"
            :options="level1"
        />
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
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";

export default defineComponent({
  name: 'AdminEbook',
  setup() {
    const formState = ref();
    formState.value = {};
    const ebooks = ref();
    const pagination = ref({
      current: 1,
      pageSize: 4,
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
        title: '分类',
        key: 'category',
        dataIndex: 'category'
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

    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/ebook/list", {
        params: {
          page: params.page,
          size: params.size,
          name: formState.value.name
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          ebooks.value = data.content.list;

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

    /**
     * 数组，[100, 101]对应：前端开发 / Vue
     */
    const categoryIds = ref();

    const ebook = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);

    const handleModalOk = () => {
      modalLoading.value = true;
      ebook.value.category1Id = categoryIds.value[0];
      ebook.value.category2Id = categoryIds.value[1];
      axios.post("/ebook/save", ebook.value).then((response) => {
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
      ebook.value = Tool.copy(record);
      categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id];
    };

    const save = () => {
      modalVisible.value = true;
      ebook.value = {};
    };

    const handleDelete = (id: string) => {
      axios.delete("/ebook/delete/" + id).then((response) => {
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

    const level1 =  ref();
    let categorys: any;
    /**
     * 查询所有分类
     **/
    const handleQueryCategory = () => {
      loading.value = true;
      axios.get("/category/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          categorys = data.content;
          console.log("原始数组：", categorys);

          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);
          console.log("树形结构：", level1.value);

          // 加载完分类后，再加载电子书，否则如果分类树加载很慢，则电子书渲染会报错
          handleQuery({
            page: 1,
            size: pagination.value.pageSize,
          });
        } else {
          message.error(data.message);
        }
      });
    };

    const getCategoryName = (cid: number) => {
      // console.log(cid)
      let result = "";
      categorys.forEach((item: any) => {
        if (item.id === cid) {
          // return item.name; // 注意，这里直接return不起作用
          result = item.name;
        }
      });
      return result;
    };

    onMounted(() => {
      handleQueryCategory();
    });

    return {
      ebooks,
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
      ebook,
      handleModalOk,

      formState,
      categoryIds,
      level1,
      getCategoryName
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