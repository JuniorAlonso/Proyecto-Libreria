package com.proyecto.Libreria.controlador;

import com.mercadopago.resources.preference.Preference;
import com.proyecto.Libreria.entidad.Pago;
import com.proyecto.Libreria.entidad.Usuario;
import com.proyecto.Libreria.repositorio.PagoRepositorio;
import com.proyecto.Libreria.service.MercadoPagoService;
import com.proyecto.Libreria.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/suscripcion")
public class SuscripcionControlador {

    private final MercadoPagoService mercadoPagoService;
    private final UsuarioService usuarioService;
    private final PagoRepositorio pagoRepositorio;

    public SuscripcionControlador(MercadoPagoService mercadoPagoService, UsuarioService usuarioService,
            PagoRepositorio pagoRepositorio) {
        this.mercadoPagoService = mercadoPagoService;
        this.usuarioService = usuarioService;
        this.pagoRepositorio = pagoRepositorio;
    }

    @GetMapping
    public String verSuscripcion(Model model, HttpSession session, @RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("error", "Error al procesar la solicitud: " + error);
        }
        try {
            Usuario usuarioSession = (Usuario) session.getAttribute("usuario");

            if (usuarioSession != null) {
                // Recargar usuario desde BD para tener datos frescos
                Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(usuarioSession.getId());

                if (usuarioOpt.isPresent()) {
                    Usuario usuario = usuarioOpt.get();
                    model.addAttribute("usuario", usuario);
                    if (usuario.tieneMembresiaActiva()) {
                        return "redirect:/"; // Si ya tiene membresía, al home
                    }
                }
            }
            return "usuario/suscripcion";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/?error=ErrorCargandoSuscripcion";
        }
    }

    @GetMapping("/crear")
    public RedirectView crearPreferencia(HttpSession session) {
        try {
            Usuario usuarioSession = (Usuario) session.getAttribute("usuario");

            if (usuarioSession != null) {
                Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(usuarioSession.getId());

                if (usuarioOpt.isPresent()) {
                    Preference preference = mercadoPagoService.crearPreferenciaMembresia(usuarioOpt.get());
                    if (preference != null) {
                        return new RedirectView(preference.getInitPoint());
                    }
                }
            }
            return new RedirectView("/suscripcion?error=true");
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("/suscripcion?error=ExcepcionCreandoPreferencia");
        }
    }

    @GetMapping("/exito")
    public String pagoExitoso(
            @RequestParam("collection_id") String collectionId,
            @RequestParam("collection_status") String collectionStatus,
            @RequestParam("payment_id") String paymentId,
            @RequestParam("status") String status,
            @RequestParam("external_reference") String externalReference,
            @RequestParam("payment_type") String paymentType,
            Model model) {

        try {
            if ("approved".equals(status)) {
                Long usuarioId = Long.parseLong(externalReference);

                // Verificar si el pago ya fue registrado para evitar duplicados
                if (pagoRepositorio.findByMercadoPagoId(paymentId) == null) {
                    // Registrar pago
                    Pago pago = new Pago();
                    pago.setMercadoPagoId(paymentId);
                    pago.setMonto(new BigDecimal("10.00")); // Debería venir de la preferencia o IPN
                    pago.setFechaPago(LocalDateTime.now());
                    pago.setEstado(status);
                    pago.setTipoPago(paymentType);

                    Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(usuarioId);
                    if (usuarioOpt.isPresent()) {
                        pago.setUsuario(usuarioOpt.get());
                        pagoRepositorio.save(pago);

                        // Renovar membresía
                        usuarioService.renovarMembresia(usuarioId);
                    }
                }
                return "usuario/pago-exitoso";
            }
            return "redirect:/suscripcion/fallo";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/suscripcion/fallo?error=ExcepcionProcesandoPago";
        }
    }

    @GetMapping("/fallo")
    public String pagoFallido() {
        return "usuario/pago-fallido";
    }

    @GetMapping("/pendiente")
    public String pagoPendiente() {
        return "usuario/pago-fallido"; // Por ahora tratamos pendiente como fallo
    }
}
