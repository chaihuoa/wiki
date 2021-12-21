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
            <a-button type="primary" @click="handleQuery()">
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

      <a-table v-if="level1.length > 0"
               :columns="columns"
               :row-key="record => record.id"
               :loading="loading"
               :data-source="level1"
               :pagination="false"
               :defaultExpandAllRows="true"
      >
        <template #headerCell="{ column }">
        </template>

        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'cover'">
            <img v-if="column.key === 'cover'" :src="record.cover" alt="avatar" />
          </template>
          <template v-else-if="column.key === 'action'">
            <a-space size="small">
              <a-button type="primary" @click="edit(record)">
                编辑
              </a-button>

              <a-popconfirm
                  title="Are you sure delete this category?"
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
      title="分类表单"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >

    <a-form :model="category" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="名称">
        <a-input v-model:value="category.name" />
      </a-form-item>
      <a-form-item label="父分类">
        <a-select
            v-model:value="category.parent"
            ref="select"
        >
          <a-select-option :value="0">
            无
          </a-select-option>
          <a-select-option v-for="c in level1" :key="c.id" :value="c.id" :disabled="category.id === c.id">
            {{c.name}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="category.sort" />
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
  name: 'AdminCategory',
  setup() {
    const formState = ref();
    formState.value = {};
    const categorys = ref();
    const loading = ref(false);

    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        key: 'name'
      },
      // {
      //   title: '父分类',
      //   key: 'parent',
      //   dataIndex: 'parent'
      // },
      {
        title: '顺序',
        key: 'sort',
        dataIndex: 'sort'
      },
      {
        title: 'Action',
        key: 'action'
      }
    ];

    /**
     * 一级分类树，children属性就是二级分类
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     */
    const level1 = ref(); // 一级分类树，children属性就是二级分类
    level1.value = [];

    const handleQuery = () => {
      loading.value = true;
      axios.get("/category/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          categorys.value = data.content;

          level1.value = [];
          level1.value = Tool.array2Tree(categorys.value, 0);
        } else {
          message.error(data.message);
        }
      });
    };

    // -------- Form ---------
    const category = ref({});
    const modalVisible = ref(false);
    const modalLoading = ref(false);

    const handleModalOk = () => {
      modalLoading.value = true;
      axios.post("/category/save", category.value).then((response) => {
        const data = response.data;
        modalLoading.value = false;

        if (data.success) {
          modalVisible.value = false;
          handleQuery();
        } else {
          message.error(data.message);
        }
      });
    };

    const edit = (record: any) => {
      modalVisible.value = true;
      category.value = Tool.copy(record);
    };

    const save = () => {
      modalVisible.value = true;
      category.value = {};
    };

    const handleDelete = (id: string) => {
      axios.delete("/category/delete/" + id).then((response) => {
        const data = response.data;

        if (data.success) {
          handleQuery();
        } else {
          message.error(data.message);
        }
      });
    };

    onMounted(() => {
      handleQuery();
    });

    return {
      // categorys,
      level1,
      columns,
      loading,

      edit,
      save,
      handleDelete,
      handleQuery,

      modalVisible,
      modalLoading,
      category,
      handleModalOk,

      formState
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