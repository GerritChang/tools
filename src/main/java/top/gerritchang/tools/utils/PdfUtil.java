package top.gerritchang.tools.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import top.gerritchang.tools.entity.PdfEntity;

import java.io.File;
import java.io.FileOutputStream;

public class PdfUtil {

    public static boolean exportPdf(PdfEntity entity) {
        Rectangle rectPageSize = new Rectangle(PageSize.A4);
        Document document = new Document(rectPageSize, entity.getMarginLeft() != 0 ? entity.getMarginLeft() : 10,
                entity.getMarginRight() != 0 ? entity.getMarginRight() : 10, entity.getMarginTop() != 0 ? entity.getMarginTop() : 50,
                entity.getMarginBottom() != 0 ? entity.getMarginBottom() : 50);

        File file = new File(entity.getFullFilePath());
        //设置纸张方向
        if (entity.getPaperDirection() == 1) {
            document.setPageSize(rectPageSize.rotate());
        }
        document.newPage();
        PdfPTable table = new PdfPTable(entity.getColums());
        table.setWidthPercentage(100);
        table.setSpacingAfter(10f);
        table.setSpacingBefore(10f);
        table.getDefaultCell().setBorder(0);
        try {
            table.setWidths(entity.getColumnWidths());
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            writer.setPageEmpty(false);
            if (!file.exists()) {
                file.createNewFile();
            }
            document.open();
            Font titleFont = new Font(BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED), 16);
            Font contentFont = new Font(BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED), 8);
            Phrase phrase = new Phrase(entity.getTitle(), titleFont);
            PdfPCell cell = new PdfPCell(phrase);
            cell.setColspan(entity.getColums());
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            PdfPCell cell1 = new PdfPCell(new Phrase(""));
            cell1.setColspan(entity.getColums());
            cell1.setBorder(Rectangle.NO_BORDER);
            cell1.setFixedHeight(20f);
            table.addCell(cell1);

            if (entity.getLittleTitleColSpan() != null && entity.getLittleTitleColSpan().length > 0) {
                for (int i = 0, j = 0; i < entity.getLittleTitleColSpan().length; i += 2, j++) {
                    PdfPCell titleCell = new PdfPCell(new Phrase(entity.getLittleTitleList()[j], contentFont));
                    titleCell.setColspan(entity.getLittleTitleColSpan()[i]);
                    titleCell.setRowspan(entity.getLittleTitleRowSpan()[i]);
                    titleCell.setBorder(Rectangle.NO_BORDER);
                    titleCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(titleCell);

                    PdfPCell contentCell = new PdfPCell(new Phrase(entity.getLittleTitleContent().get(entity.getLittletitleParam()[j]).toString(), contentFont));
                    contentCell.setColspan(entity.getLittleTitleColSpan()[i + 1]);
                    contentCell.setRowspan(entity.getLittleTitleRowSpan()[i + 1]);
                    contentCell.setBorder(Rectangle.NO_BORDER);
                    contentCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(contentCell);
                }
            }

            if (entity.getTableTitle() != null && entity.getTableTitle().length > 0) {
                for (int i = 0; i < entity.getTableTitle().length; i++) {
                    PdfPCell tableTitleCell = new PdfPCell(new Phrase(entity.getTableTitle()[i], contentFont));
                    tableTitleCell.setColspan(entity.getTableTitleColSpan()[i]);
                    tableTitleCell.setRowspan(entity.getTableTitleRowSpan()[i]);
                    tableTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tableTitleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(tableTitleCell);
                }
            }

            if (entity.getList() != null && entity.getList().size() > 0) {
                for (int i = 0; i < entity.getList().size(); i++) {
                    for (int j = 0; j < entity.getColums(); j++) {
                        String param = entity.getTableParam()[j];
                        if (param.equals("XH")) {
                            PdfPCell tableContentCell = new PdfPCell(new Phrase(i + 1 + "", contentFont));
                            tableContentCell.setHorizontalAlignment(setHorizontalAlignment(entity.getTableAlignStyle()[j]));
                            tableContentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            table.addCell(tableContentCell);
                        } else {
                            PdfPCell tableContentCell = new PdfPCell(new Phrase(entity.getList().get(i).get(param).toString(), contentFont));
                            tableContentCell.setHorizontalAlignment(setHorizontalAlignment(entity.getTableAlignStyle()[j]));
                            tableContentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            table.addCell(tableContentCell);
                        }
                    }
                }
            }

            if (entity.getBottomColSpan() != null && entity.getBottomColSpan().length > 0) {
                for (int i = 0, j = 0; i < entity.getBottomColSpan().length; i += 2, j++) {
                    PdfPCell bottomCell = new PdfPCell(new Phrase(entity.getBottomTitleList()[j].toString(), contentFont));
                    bottomCell.setColspan(entity.getBottomColSpan()[i]);
                    bottomCell.setRowspan(entity.getBottomRowSpan()[i]);
                    if (entity.getBottomBorder() == null || entity.getBottomBorder().length == 0) {
                        bottomCell.setBorder(Rectangle.NO_BORDER);
                    } else {
                        if (entity.getBottomBorder()[i] == 0) {
                            bottomCell.setBorder(Rectangle.NO_BORDER);
                        }
                    }
                    bottomCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(bottomCell);

                    PdfPCell bottomContentCell = new PdfPCell(new Phrase(entity.getBottomContent().get(entity.getBottomParamList()[j]).toString(), contentFont));
                    bottomContentCell.setColspan(entity.getBottomColSpan()[i + 1]);
                    bottomContentCell.setRowspan(entity.getBottomRowSpan()[i + 1]);
                    if (entity.getBottomBorder() == null || entity.getBottomBorder().length == 0) {
                        bottomContentCell.setBorder(Rectangle.NO_BORDER);
                    } else {
                        if (entity.getBottomBorder()[i + 1] == 0) {
                            bottomContentCell.setBorder(Rectangle.NO_BORDER);
                        }
                    }
                    bottomContentCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(bottomContentCell);
                }
            }
            document.add(table);
            document.close();
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static int setHorizontalAlignment(String str) {
        int number = 0;
        switch (str) {
            case "left":
                number = Element.ALIGN_LEFT;
                break;
            case "center":
                number = Element.ALIGN_CENTER;
                break;
            case "right":
                number = Element.ALIGN_RIGHT;
                break;
            default:
                number = Element.ALIGN_CENTER;
        }
        return number;
    }

}

