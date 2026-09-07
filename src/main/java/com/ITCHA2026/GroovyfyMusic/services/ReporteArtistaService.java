package com.ITCHA2026.GroovyfyMusic.services;

import com.ITCHA2026.GroovyfyMusic.dto.ReporteCancionDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Cancion;
import com.ITCHA2026.GroovyfyMusic.entities.Reproduccion;
import com.ITCHA2026.GroovyfyMusic.entities.Usuario;
import com.ITCHA2026.GroovyfyMusic.exceptions.BadRequestException;
import com.ITCHA2026.GroovyfyMusic.exceptions.ResourceNotFoundException;
import com.ITCHA2026.GroovyfyMusic.interfaces.IReproduccionRepository;
import com.ITCHA2026.GroovyfyMusic.repository.UsuarioRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteArtistaService {

    private final IReproduccionRepository reproduccionRepository;
    private final UsuarioRepository usuarioRepository;

    private static final float CM = 28.3465f;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Color morado de la marca Groovify (reemplaza el verde de Spotify)
    private static final BaseColor COLOR_PRIMARIO = new BaseColor(154, 69, 242);

    public byte[] generarReporte(Integer artistaId, LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new BadRequestException("Seleccione un rango de fechas válido");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new BadRequestException("La fecha de inicio no puede ser posterior a la fecha fin");
        }

        Usuario artista = usuarioRepository.findById(artistaId)
                .orElseThrow(() -> new ResourceNotFoundException("Artista no encontrado"));

        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);

        List<Reproduccion> reproducciones = reproduccionRepository
                .findByCancionArtistaIdAndReproducidoEnBetween(artistaId, inicio, fin);

        int totalOyentes = (int) reproducciones.stream()
                .map(r -> r.getUsuario().getId())
                .distinct()
                .count();

        Map<Cancion, Long> conteoPorCancion = reproducciones.stream()
                .collect(Collectors.groupingBy(Reproduccion::getCancion, Collectors.counting()));

        List<ReporteCancionDTO> cancionesDTO = conteoPorCancion.entrySet().stream()
                .map(entry -> {
                    Cancion c = entry.getKey();
                    String album = (c.getAlbum() != null) ? c.getAlbum().getNombre() : "Sencillo";

                    ReporteCancionDTO dto = new ReporteCancionDTO();
                    dto.setNombreCancion(c.getNombre());
                    dto.setNombreAlbum(album);
                    dto.setTotalReproducciones(entry.getValue().intValue());

                    return dto;
                })
                .sorted((a, b) -> Integer.compare(b.getTotalReproducciones(), a.getTotalReproducciones()))
                .toList();

        try {
            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            Document document = new Document(PageSize.LETTER, 2.5f * CM, 2.5f * CM, 2.5f * CM, 2.5f * CM);
            PdfWriter writer = PdfWriter.getInstance(document, salida);

            String fechaGeneracion = LocalDate.now().format(FORMATO_FECHA);
            writer.setPageEvent(new FooterEvent(fechaGeneracion));

            document.open();
            agregarEncabezado(document, artista.getAlias(), fechaInicio, fechaFin);
            agregarResumen(document, cancionesDTO, totalOyentes);
            agregarDetalle(document, cancionesDTO);

            document.close();
            return salida.toByteArray();
        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar el PDF del reporte", e);
        }
    }

    private void agregarEncabezado(Document document, String nombreArtista, LocalDate fechaInicio, LocalDate fechaFin) throws DocumentException {
        PdfPTable tablaEncabezado = new PdfPTable(1);
        tablaEncabezado.setWidthPercentage(100);

        PdfPCell celdaTexto = new PdfPCell();
        celdaTexto.setBorder(Rectangle.NO_BORDER);
        celdaTexto.setVerticalAlignment(Element.ALIGN_MIDDLE);

        Font fontEmpresa = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, COLOR_PRIMARIO);
        celdaTexto.addElement(new Paragraph("Groovyfy Music - Panel de Artista", fontEmpresa));

        Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
        Paragraph titulo = new Paragraph("Reporte de Reproducciones: " + nombreArtista, fontTitulo);
        titulo.setSpacingBefore(2);
        celdaTexto.addElement(titulo);

        Font fontSubTitulo = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.GRAY);
        Paragraph rango = new Paragraph("Período: " + fechaInicio.format(FORMATO_FECHA) + " - " + fechaFin.format(FORMATO_FECHA), fontSubTitulo);
        rango.setSpacingBefore(2);
        celdaTexto.addElement(rango);

        tablaEncabezado.addCell(celdaTexto);
        document.add(tablaEncabezado);

        Paragraph espacio = new Paragraph(" ");
        espacio.setSpacingAfter(6);
        document.add(espacio);
    }

    private void agregarResumen(Document document, List<ReporteCancionDTO> canciones, int oyentesUnicos) throws DocumentException {
        Font fontSeccion = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
        Paragraph titulo = new Paragraph("Resumen del Mes", fontSeccion);
        titulo.setSpacingBefore(10);
        titulo.setSpacingAfter(6);
        document.add(titulo);

        PdfPTable tabla = new PdfPTable(new float[]{3f, 2f});
        tabla.setWidthPercentage(60);
        tabla.setHorizontalAlignment(Element.ALIGN_LEFT);

        int totalReproducciones = canciones.stream()
                .mapToInt(ReporteCancionDTO::getTotalReproducciones)
                .sum();

        Font fontFila = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
        Font fontTotal = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);

        tabla.addCell(celdaTexto("Total Oyentes Únicos", fontFila));
        tabla.addCell(celdaTexto(String.valueOf(oyentesUnicos), fontTotal));

        tabla.addCell(celdaTexto("Total Reproducciones", fontFila));
        tabla.addCell(celdaTexto(String.valueOf(totalReproducciones), fontTotal));

        document.add(tabla);
    }

    private void agregarDetalle(Document document, List<ReporteCancionDTO> canciones) throws DocumentException {
        Font fontSeccion = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
        Paragraph titulo = new Paragraph("Canciones Más Reproducidas (" + canciones.size() + ")", fontSeccion);
        titulo.setSpacingBefore(16);
        titulo.setSpacingAfter(6);
        document.add(titulo);

        if (canciones.isEmpty()) {
            Font fontVacio = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);
            document.add(new Paragraph("No se registraron reproducciones en el rango de fechas seleccionado.", fontVacio));
            return;
        }

        document.add(construirTablaCanciones(canciones));
    }

    private PdfPTable construirTablaCanciones(List<ReporteCancionDTO> canciones) throws DocumentException {
        PdfPTable tabla = new PdfPTable(new float[]{4f, 3f, 2f});
        tabla.setWidthPercentage(100);

        Font fontEncabezado = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.WHITE);
        for (String columna : new String[]{"Canción", "Álbum", "Reproducciones"}) {
            PdfPCell celda = new PdfPCell(new Phrase(columna, fontEncabezado));
            celda.setBackgroundColor(COLOR_PRIMARIO);
            celda.setPadding(5);
            tabla.addCell(celda);
        }

        Font fontFila = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
        for (ReporteCancionDTO c : canciones) {
            tabla.addCell(celdaTexto(c.getNombreCancion(), fontFila));
            tabla.addCell(celdaTexto(c.getNombreAlbum(), fontFila));
            tabla.addCell(celdaTexto(String.valueOf(c.getTotalReproducciones()), fontFila));
        }

        return tabla;
    }

    private PdfPCell celdaTexto(String texto, Font font) {
        PdfPCell celda = new PdfPCell(new Phrase(texto != null ? texto : "-", font));
        celda.setPadding(4);
        return celda;
    }

    private static class FooterEvent extends PdfPageEventHelper {
        private final String fechaGeneracion;

        FooterEvent(String fechaGeneracion) {
            this.fechaGeneracion = fechaGeneracion;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            Font fontFooter = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.GRAY);

            Phrase footerIzquierda = new Phrase("Fecha Generación: " + fechaGeneracion, fontFooter);
            Phrase footerDerecha = new Phrase("Página: " + writer.getPageNumber(), fontFooter);

            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, footerIzquierda, document.leftMargin(), document.bottom() - 20, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, footerDerecha, document.right(), document.bottom() - 20, 0);
        }
    }
}