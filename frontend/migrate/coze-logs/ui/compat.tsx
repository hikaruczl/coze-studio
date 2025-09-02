/*
 * Coze Logs Migration - UI Compat Layer (Ant Design)
 * 将原 @coze-arch/coze-design 依赖映射到 AntD 组件，便于迁移后运行。
 * 如需更贴近原交互，可在这里逐步细化包装。
 */
import React from 'react';
import { Tabs, Tag, Tooltip, Typography, Button } from 'antd';

// SegmentTab 近似映射为 Tabs
// 注意：原代码使用 <SegmentTab><SegmentTab.Tab value=...>children</SegmentTab.Tab>...</SegmentTab>
// AntD v5 推荐使用 items，但为了兼容我们提供一个 children 兼容的轻量包装。
export const SegmentTab = Object.assign(
  (
    props: React.ComponentProps<typeof Tabs> & {
      children?: React.ReactNode;
      value?: any;
      onChange?: (e: any) => void;
    },
  ) => {
    const { value, onChange, children, ...rest } = props as any;
    // 将 onChange(e) 或 onChange({ target: { value } }) 的调用规范化
    const handleChange = (activeKey: any) => {
      if (typeof onChange === 'function') {
        onChange({ target: { value: activeKey } });
      }
    };
    return (
      <Tabs activeKey={String(value ?? rest.defaultActiveKey)} onChange={handleChange} {...rest}>
        {children}
      </Tabs>
    );
  },
  {
    Tab: ({ value, children }: { value: string | number; children?: React.ReactNode }) => (
      // 提供一个简易的 Tab 包装（仅在 children 模式下使用）
      // 实际渲染时将由父级 <Tabs> 解析；如需完整支持，建议改为使用 Tabs items。
      <Tabs.TabPane tab={children} key={String(value)} />
    ),
  },
);

export { Tag, Tooltip, Typography };
export const IconButton: React.FC<React.ComponentProps<typeof Button>> = props => (
  <Button type="text" size="small" {...props} />
);

