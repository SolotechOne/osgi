package osgi.service.factory.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShellTable {
    public List<String> header = new ArrayList<String>();
    public List<List<String>> content = new ArrayList<List<String>>();
    public int maxColSize = 25;
    
    public  List<String> addRow() {
        List<String> row = new ArrayList<String>();
        content.add(row);
        return row;
    }
    
    public  void print()  {
        int[] sizes = new int[header.size()];
        updateSizes(sizes, header);
        for (List<String> row : content) {
            updateSizes(sizes, row);
        }
        String headerLine = getRow(sizes, header, " | ");
        System.out.println(headerLine);
        System.out.println(underline(headerLine.length()));
        for (List<String> row : content) {
            System.out.println(getRow(sizes, row, " | "));
        }
    }

    private  String underline(int length) {
        char[] exmarks = new char[length];
        Arrays.fill(exmarks, '-');
        return new String(exmarks);
    }

    private  String getRow(int[] sizes, List<String> row, String separator) {
        StringBuilder line = new StringBuilder();
        int c = 0;
        for (String cell : row) {
            if (cell == null) {
                cell = "";
            }
            if (cell.length() > maxColSize) {
                cell = cell.substring(0, maxColSize -1);
            }
            cell = cell.replaceAll("\n", "");
            line.append(String.format("%-" + sizes[c] + "s", cell));
            if (c + 1 < row.size()) {
                line.append(separator);
            }
            c++;
        }
        return line.toString();
    }

    private  void updateSizes(int[] sizes, List<String> row) {
        int c = 0;
        for (String cellContent : row) {
            int cellSize = cellContent != null ? cellContent.length() : 0;
            cellSize = Math.min(cellSize, maxColSize);
            if (cellSize > sizes[c]) {
                sizes[c] = cellSize;
            }
            c++;
        }
    }
}