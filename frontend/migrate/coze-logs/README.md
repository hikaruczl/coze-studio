# Coze 工作流「日志」标签页迁移包

本目录包含从 coze-studio 前端中抽取的“工作流详情页 → 日志”标签页 UI 组件与日志解析能力（不含“分析/Trace”）。

目标：在你的项目（React + TypeScript + Ant Design + axios）中复用日志标签页能力。

## 目录结构

- api/
  - workflow.ts：GetNodeExecuteHistory 接口兼容层（axios）。
- ui/
  - compat.tsx：UI 兼容层，将 SegmentTab/Tag/Typography/Tooltip/IconButton 映射到 AntD。
- i18n/
  - compat.ts：I18n.t 兼容函数（可替换为你的 i18n 系统）。
- types/
  - workflow.ts：最小 NodeResult / NodeExeStatus 类型定义（如未使用原包）。
- features/log/：日志解析与渲染组件族（DataViewer、MarkdownViewer、各类 Log Parser、工具函数等）。
- components/log-detail/：日志详情容器（分页、仅错误筛选、图片日志、字段渲染）。

## 依赖

- React + TypeScript
- axios（api/workflow.ts 使用）
- Ant Design（ui/compat.tsx 使用）
- 可选：react-query（如果使用原始 hook 逻辑；也可改造为你的数据层）
- lodash-es（少量工具函数）
- less + css modules（*.module.less）

## 集成步骤（通用）

1) 安装依赖（如未安装）
- axios、antd、lodash-es
- 若保留 hook 内的 react-query：@tanstack/react-query
- 构建工具启用 less 与 css modules

2) API 对接
- 编辑 api/workflow.ts，将 URL '/api/workflow/node/execute_history' 改为你的后端接口地址，返回体适配为 NodeResult 结构（见 types/workflow.ts）。

3) UI 适配
- ui/compat.tsx 已将 SegmentTab/Tag 等映射到 AntD，如需更贴近原交互，可在此处细化包装。

4) i18n
- i18n/compat.ts 暂返回 key，可替换为你的 i18n 实现。

5) 页面挂载
- 在工作流详情页 Tabs 中新增“日志”页签，渲染 components/log-detail/log-detail.tsx 的导出组件（LogDetail）。
- 传入 spaceId、workflowId、初始 result（可选）与 onOpenWorkflowLink（可选）。

示例：

```tsx
import { LogDetail } from '@/migrate/coze-logs/components/log-detail/log-detail';

export default function WorkflowLogsTab(props: { spaceId: string; workflowId: string }) {
  return (
    <LogDetail
      result={{ /* 可传空，或传最后一次运行结果 */ }}
      spaceId={props.spaceId}
      workflowId={props.workflowId}
      onOpenWorkflowLink={({ workflowId, executeId, subExecuteId }) => {
        // TODO: 在你的项目中实现子流程跳转
      }}
    />
  );
}
```

6) 可选：搜索能力
- 原组件未内置搜索输入框。如需“搜索”，建议在 Logs Tab 外围添加 keyword 状态，对生成的 logs 做客户端过滤（不改动迁移组件内部）。

伪代码：

```tsx
const [kw, setKw] = useState('');
// 在渲染 <LogFields> 上层，将 logs 过滤后再渲染（需要在 components/log-detail/log-fields/index.tsx 处插入过滤逻辑，或在外层封装一个 LogDetailWithSearch）。
```

## 验收清单
- 分页、仅显示错误筛选可用
- 输入/输出/错误信息完整展示
- 输出 Tab 可切换“最终输出/原始输出”，Markdown 预览正常
- 复制、展开/折叠正常
- 子流程链接可跳转
- 图片日志展示正常（如需）

## 注意
- 目前拷贝的源码仍保留原包 import（如 @coze-arch/i18n、@coze-arch/coze-design、@coze-workflow/base/api）。如果你的项目没有这些包，请：
  - 替换 I18n.t → i18n/compat.ts
  - 替换 UI 组件 → ui/compat.tsx
  - 替换 workflowApi.GetNodeExecuteHistory → api/workflow.ts
- 若不使用 react-query，可改造 components/log-detail/hooks/use-get-current-result.ts：将 useQuery 改为你的数据拉取逻辑，保持返回 { current, batchData } 接口不变。

## 版权
源代码遵循原仓库的开源许可证条款。请在你项目中保留相应的版权与许可声明。

