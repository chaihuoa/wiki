<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-row :gutter="24">
        <a-col :span="8">
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
                  <a-button type="primary" @click="edit(record)" size="small">
                    编辑
                  </a-button>

                  <a-popconfirm
                      title="Are you sure delete this doc?"
                      ok-text="Yes"
                      cancel-text="No"
                      @confirm="handleDelete(record.id)"
                  >
                    <a-button type="danger" size="small">
                      删除
                    </a-button>
                  </a-popconfirm>

                </a-space>
              </template>
            </template>
          </a-table>
        </a-col>
        <a-col :span="16">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="handleSave()">
                  保存
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :model="doc" :layout="vertical">
            <a-form-item>
              <a-input v-model:value="doc.name" placeholder="名称"/>
            </a-form-item>
            <a-form-item>
              <a-tree-select
                  v-model:value="doc.parent"
                  style="width: 100%"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  :tree-data="treeSelectData"
                  placeholder="请选择父文档"
                  tree-default-expand-all
                  :fieldNames="{children:'children', label:'name', key:'id', value: 'id' }"
              >
              </a-tree-select>
            </a-form-item>
            <a-form-item>
              <a-input v-model:value="doc.sort" placeholder="顺序"/>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="handlePreviewContent()">
                <EyeOutlined /> 内容预览
              </a-button>
            </a-form-item>
            <a-form-item>
              <div id="content">
              </div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
      <a-drawer width="900" placement="right" :closable="false" :visible="drawerVisible" @close="onDrawerClose">
        <div class="wangeditor" :innerHTML="previewHtml"></div>
      </a-drawer>
    </a-layout-content>
  </a-layout>

<!--  <a-modal-->
<!--      v-model:visible="modalVisible"-->
<!--      title="文档表单"-->
<!--      :confirm-loading="modalLoading"-->
<!--      @ok="handleModalOk"-->
<!--  >-->
<!--  </a-modal>-->

</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import E from 'wangeditor'

export default defineComponent({
  name: 'AdminDoc',
  setup() {
    const route = useRoute();
    console.log("路由：", route);
    console.log("route.path：", route.path);
    console.log("route.query：", route.query);
    console.log("route.param：", route.params);
    console.log("route.fullPath：", route.fullPath);
    console.log("route.name：", route.name);
    console.log("route.meta：", route.meta);
    const formState = ref();
    formState.value = {};
    const docs = ref();
    const loading = ref(false);

    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        key: 'name'
      },
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
      axios.get("/doc/all/" + route.query.ebookId).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          docs.value = data.content;

          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);

          // 父文档下拉框初始化，相当于点击新增
          treeSelectData.value = Tool.copy(level1.value) || [];
          // 为选择树添加一个"无"
          treeSelectData.value.unshift({id: 0, name: '无'});
        } else {
          message.error(data.message);
        }
      });
    };

    const handleQueryContent = () => {
      axios.get("/doc/find-content/" + doc.value.id).then((response) => {
        const data = response.data;
        if (data.success) {
          editor.txt.html(data.content);
        } else {
          message.error(data.message);
        }
      });
    };

    // -------- Form ---------
    const editor = new E('#content')
    editor.config.zIndex = 0
    const treeSelectData = ref();
    treeSelectData.value = [];
    const doc = ref();
    doc.value = {
      ebookId: route.query.ebookId
    };
    const modalVisible = ref(false);
    const modalLoading = ref(false);

    const handleSave = () => {
      modalLoading.value = true;
      doc.value.content = editor.txt.html();
      axios.post("/doc/save", doc.value).then((response) => {
        const data = response.data;
        modalLoading.value = false;

        if (data.success) {
          message.success("保存成功");
          modalVisible.value = false;
          handleQuery();
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 将某节点及其子孙节点全部置为disabled
     */
    const setDisable = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          console.log("disabled", node);
          // 将目标节点设置为disabled
          node.disabled = true;

          // 遍历所有子节点，将所有子节点全部都加上disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    };

    const deleteIds: Array<string> = [];
    /**
     * 查找整根树枝
     */
    const getDeleteIds = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        // 1.如果当前节点就是目标节点
        if (node.id === id) {
          console.log("delete", node);
          // 将目标ID放入结果集ids
          // node.disabled = true;
          deleteIds.push(id);

          // 遍历所有子节点
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIds(children, children[j].id)
            }
          }
        } else {
          // 2.如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
          }
        }
      }
    };

    const edit = (record: any) => {
      editor.txt.html("");
      modalVisible.value = true;
      doc.value = Tool.copy(record);
      handleQueryContent();

      // 不能选择当前节点及其所有子孙节点，作为父节点，会使树断开
      treeSelectData.value = Tool.copy(level1.value) || [];
      setDisable(treeSelectData.value, record.id);

      // 为选择树添加一个"无"
      treeSelectData.value.unshift({id: 0, name: '无'});
    };

    const save = () => {
      editor.txt.html("");
      modalVisible.value = true;

      // 不能选择当前节点及其所有子孙节点，作为父节点，会使树断开
      treeSelectData.value = Tool.copy(level1.value) || [];

      // 为选择树添加一个"无"
      treeSelectData.value.unshift({id: 0, name: '无'});
    };

    const handleDelete = (id: string) => {
      deleteIds.length = 0;
      getDeleteIds(level1.value, id);
      axios.delete("/doc/delete/" + deleteIds.join(",")).then((response) => {
        const data = response.data;

        if (data.success) {
          handleQuery();
        } else {
          message.error(data.message);
        }
      });
    };

    // ----------------富文本预览--------------
    const drawerVisible = ref(false);
    const previewHtml = ref();
    const handlePreviewContent = () => {
      const html = editor.txt.html();
      previewHtml.value = html;
      drawerVisible.value = true;
    };
    const onDrawerClose = () => {
      drawerVisible.value = false;
    };

    onMounted(() => {
      editor.create()
      handleQuery();
    });

    return {
      // docs,
      level1,
      columns,
      loading,

      edit,
      save,
      handleDelete,
      handleQuery,

      modalVisible,
      modalLoading,
      doc,
      handleSave,

      formState,
      treeSelectData,

      drawerVisible,
      previewHtml,
      handlePreviewContent,
      onDrawerClose,
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