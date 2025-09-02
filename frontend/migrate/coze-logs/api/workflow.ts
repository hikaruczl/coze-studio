/*
 * Coze Logs Migration - API Compat Layer
 * 提供与 workflowApi.GetNodeExecuteHistory 等价的调用封装，便于在你的项目中适配后端接口。
 * 使用 axios，按需修改 URL 与返回体适配逻辑。
 */

import axios from 'axios';

export interface GetNodeExecuteHistoryParams {
  workflow_id: string;
  space_id: string;
  execute_id: string;
  node_id: string;
  node_type: string; // 与后端约定的 node 类型字符串
  is_batch?: boolean;
  batch_index?: number;
  node_history_scene?: string; // 可选：TestRunInput 等
}

// 与迁移组件期望的最小 NodeResult 结构保持一致（可根据后端返回体扩展）
export interface NodeResultCompat {
  index?: number;
  isBatch?: boolean;
  needAsync?: boolean;
  executeId?: string;
  subExecuteId?: string;
  nodeId?: string;
  NodeType?: string;
  nodeStatus?: string;
  input?: any;
  output?: any;
  errorInfo?: any;
  batch?: string; // JSON 字符串化的批次结果
}

export async function getNodeExecuteHistory(
  params: GetNodeExecuteHistoryParams,
): Promise<NodeResultCompat> {
  // TODO: 将此 URL 替换为你后端的真实接口路径；或改为 POST
  const { data } = await axios.get('/api/workflow/node/execute_history', {
    params,
  });

  // 如果你的后端返回结构为 { data: {...} }，可在这里做一层适配
  return (data?.data ?? data) as NodeResultCompat;
}

