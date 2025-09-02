/*
 * Coze Logs Migration - Type Compat Layer
 * 最小 NodeResult / NodeExeStatus 定义以便在未引入原包时编译通过。
 * 可根据实际后端返回体扩展字段。
 */

export type NodeExeStatus = 'Success' | 'Failed' | 'Running' | 'Unknown';

export interface NodeResult {
  index?: number;
  isBatch?: boolean;
  needAsync?: boolean;
  executeId?: string;
  subExecuteId?: string;
  nodeId?: string;
  NodeType?: string;
  nodeStatus?: NodeExeStatus;
  input?: any;
  output?: any;
  errorInfo?: any;
  batch?: string; // JSON 字符串化的批次结果
}

