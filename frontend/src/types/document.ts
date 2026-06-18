import type { DocumentMetadataValue } from './metadata';

export type Document = {
  id: number;
  name: string;
  fileType: string;
  metadata: DocumentMetadataValue[];
};
