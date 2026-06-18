export type DocumentMetadataValue = {
  fieldId: number;
  key: string;
  type: 'string' | 'number' | 'boolean' | 'date';
  value: string | number | boolean | Date;
};
