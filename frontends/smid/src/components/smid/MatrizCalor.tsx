import { Table, Text } from '@mantine/core';

interface MatrizCalorProps {
  matriz: Record<string, Record<string, number>>;
  derechos: string[];
}

const getCellStyle = (count: number) => {
  if (count === 0) return { backgroundColor: '#f8fafc', color: '#cbd5e1' };
  if (count < 3) return { backgroundColor: '#fef3c7', color: '#92400e', border: '1px solid #fde68a' };
  return { backgroundColor: '#fee2e2', color: '#b91c1c', border: '1px solid #fecaca' };
};

export const MatrizCalor = ({ matriz, derechos }: MatrizCalorProps) => {
  const instituciones = Object.keys(matriz);

  if (instituciones.length === 0) {
    return <Text c="dimmed" ta="center" py="xl">No hay datos suficientes para la matriz.</Text>;
  }

  return (
    <Table.ScrollContainer minWidth={600}>
      <Table withTableBorder withColumnBorders verticalSpacing="xs">
        <Table.Thead className="bg-gray-50">
          <Table.Tr>
            <Table.Th className="text-xs text-gray-500 uppercase">Garante / Vulneración</Table.Th>
            {derechos.map((d) => (
              <Table.Th key={d} className="text-center text-[10px] text-gray-500 uppercase">{d}</Table.Th>
            ))}
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>
          {instituciones.map((inst) => (
            <Table.Tr key={inst}>
              <Table.Td className="font-bold text-sm bg-gray-50/50">{inst}</Table.Td>
              {derechos.map((der) => {
                const count = matriz[inst]?.[der] || 0;
                return (
                  <Table.Td key={der} className="text-center p-1">
                    <div
                      className="py-1.5 rounded font-bold text-sm"
                      style={getCellStyle(count)}
                    >
                      {count > 0 ? count : '-'}
                    </div>
                  </Table.Td>
                );
              })}
            </Table.Tr>
          ))}
        </Table.Tbody>
      </Table>
    </Table.ScrollContainer>
  );
};
